<template>
  <div class="transcricao-container">
    <div class="header">
      <h1>🎙️ Transcrição de Áudio</h1>
      <div class="status-badge" :class="{ 'connected': conectado, 'disconnected': !conectado }">
        {{ conectado ? 'Conectado' : 'Desconectado' }}
      </div>
    </div>

    <div class="controls">
      <button 
        class="btn btn-primary" 
        @click="iniciarGravacao" 
        :disabled="!conectado || recorder"
        :class="{ 'disabled': !conectado || recorder }"
      >
        <span class="icon">▶️</span> Iniciar Gravação
      </button>
      <button 
        class="btn btn-danger" 
        @click="pararGravacao" 
        :disabled="!conectado || !recorder"
        :class="{ 'disabled': !conectado || !recorder }"
      >
        <span class="icon">⏹️</span> Parar Gravação
      </button>
    </div>

    <div class="results-container">
      <div class="result-card">
        <h2>🧠 Transcrição</h2>
        <div class="content">{{ transcricao }}</div>
      </div>

      <div class="result-card">
        <h2>📝 Resumo</h2>
        <div class="content">{{ resumo }}</div>
      </div>

      <div class="result-card">
        <h2>📝 Extrato</h2>
        <div class="content">{{ extrato }}</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref} from 'vue'
import SockJS from 'sockjs-client'

const conectado = ref(false)
const recorder = ref(null)
const transcricao = ref('Aguardando transcrição...')
const resumo = ref('Aguardando resumo...')
const extrato = ref('Aguardando extrato...')
const reconnectAttempts = ref(0)
const maxReconnectAttempts = 5
const reconnectInterval = 3000 // 3 seconds
const pingInterval = 30000 // 30 seconds

let socket = null
let reconnectTimer = null
let pingTimer = null

const createSocket = () => {
  if (socket) {
    // Clean up existing socket if any
    try {
      socket.close()
    } catch (e) {
      console.error('Error closing existing socket', e)
    }
  }

  socket = new SockJS('/ws-transcricao')

  socket.onopen = () => {
    console.log('WebSocket connection established')
    habilitar()
    reconnectAttempts.value = 0

    // Start ping interval
    clearInterval(pingTimer)
    pingTimer = setInterval(() => {
      if (socket && socket.readyState === SockJS.OPEN) {
        try {
          socket.send('PING')
        } catch (e) {
          console.error('Error sending ping', e)
        }
      }
    }, pingInterval)
  }

  socket.onmessage = e => {
    if (e.data === 'PONG') {
      console.log('Received pong from server')
      return
    }
    try {
      const data = JSON.parse(e.data)
      processarRetorno(data)
    } catch (err) {
      console.error('Error parsing message', err, e.data)
    }
  }

  socket.onclose = (event) => {
    console.log('WebSocket connection closed', event)
    desabilitar()
    clearInterval(pingTimer)

    // Attempt to reconnect if not a normal closure
    if (event.code !== 1000 && reconnectAttempts.value < maxReconnectAttempts) {
      reconnectAttempts.value++
      console.log(`Attempting to reconnect (${reconnectAttempts.value}/${maxReconnectAttempts})...`)
      clearTimeout(reconnectTimer)
      reconnectTimer = setTimeout(createSocket, reconnectInterval)
    } else if (reconnectAttempts.value >= maxReconnectAttempts) {
      console.error('Max reconnect attempts reached')
    }
  }

  socket.onerror = err => {
    console.error('Socket error', err)
  }
}

onMounted(() => {
  createSocket()
})

const habilitar = () => {
  conectado.value = true
}

const desabilitar = () => {
  conectado.value = false
}

const iniciarGravacao = async () => {
  navigator.mediaDevices.getUserMedia({audio: true})
      .then(stream => {
        let indice = 1
        recorder.value = new MediaRecorder(stream)
        recorder.value.ondataavailable = event => event.data.arrayBuffer().then(buffer => enviarAudio(indice++, buffer))
        recorder.value.onstop = event => finalizar()
        recorder.value.start(250)
      })
      .catch(err => console.error('mic error', err))
}

const processarRetorno = dado => {
  if (dado.tipo === 'transcricao') {
    transcricao.value = dado.conteudo
  } else if (dado.tipo === 'resumo') {
    resumo.value = dado.conteudo
  } else if (dado.tipo === 'extrato') {
    extrato.value = dado.conteudo
  }
}

const pararGravacao = () => {
  if (recorder.value && recorder.value.state === 'recording')
    recorder.value.stop()
}

const enviarAudio = (indice, audio) => {
  if (socket && conectado.value && socket.readyState === SockJS.OPEN) {
    try {
      let chunk = {
        indice,
        base64: btoa(String.fromCharCode(...new Uint8Array(audio)))
      }
      socket.send(JSON.stringify(chunk))
    } catch (err) {
      console.error('Error sending audio chunk', err)
      // If we encounter an error while sending, attempt to reconnect
      if (reconnectAttempts.value < maxReconnectAttempts) {
        console.log('Connection issue detected, attempting to reconnect...')
        clearTimeout(reconnectTimer)
        reconnectTimer = setTimeout(createSocket, 1000) // Quick reconnect attempt
      }
    }
  } else if (!socket || socket.readyState !== SockJS.OPEN) {
    console.warn('Cannot send audio: socket is not open')
    // Attempt to reconnect if socket is not open
    if (reconnectAttempts.value < maxReconnectAttempts) {
      console.log('Socket not open, attempting to reconnect...')
      clearTimeout(reconnectTimer)
      reconnectTimer = setTimeout(createSocket, 1000) // Quick reconnect attempt
    }
  }
}

const finalizar = () => {
  if (socket && conectado.value && socket.readyState === SockJS.OPEN) {
    try {
      socket.send('FIM')
    } catch (err) {
      console.error('Error sending FIM message', err)
    }
  }
  recorder.value = null;
}

onUnmounted(() => {
  // Stop recording if active
  if (recorder.value?.state === 'recording') {
    try {
      recorder.value.stop()
    } catch (err) {
      console.error('Error stopping recorder', err)
    }
  }

  // Clear all timers
  clearInterval(pingTimer)
  clearTimeout(reconnectTimer)

  // Close socket connection
  if (socket) {
    try {
      socket.close()
    } catch (err) {
      console.error('Error closing socket', err)
    }
    socket = null
  }
})

</script>

<style scoped>
.transcricao-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.header h1 {
  font-size: 24px;
  margin: 0;
  color: #2c3e50;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
}

.connected {
  background-color: #4caf50;
  color: white;
}

.disconnected {
  background-color: #f44336;
  color: white;
}

.controls {
  display: flex;
  gap: 15px;
  margin-bottom: 25px;
}

.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.btn .icon {
  margin-right: 8px;
}

.btn-primary {
  background-color: #2196f3;
  color: white;
}

.btn-primary:hover:not(.disabled) {
  background-color: #0d8bf2;
}

.btn-danger {
  background-color: #f44336;
  color: white;
}

.btn-danger:hover:not(.disabled) {
  background-color: #e53935;
}

.btn.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.results-container {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
}

.result-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-card h2 {
  font-size: 18px;
  margin-top: 0;
  margin-bottom: 15px;
  color: #2c3e50;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.content {
  white-space: pre-wrap;
  font-size: 15px;
  line-height: 1.5;
  color: #555;
  max-height: 300px;
  overflow-y: auto;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 5px;
}

@media (max-width: 768px) {
  .header {
    flex-direction: column;
    align-items: flex-start;
  }

  .status-badge {
    margin-top: 10px;
  }

  .controls {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
