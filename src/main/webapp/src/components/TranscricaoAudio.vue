<template>
  <div class="transcricao-container">
    <div class="header">
      <h1>🎙️ Transcrição de Áudio</h1>
      <div class="client-id">ID do Cliente: {{ clientUuid }}</div>
    </div>

    <div class="controls">
      <button
          :class="{ 'disabled': !conectado || recorder }"
          :disabled="!conectado || recorder"
          class="btn btn-primary"
          @click="iniciarGravacao"
      >
        <span class="icon">▶️</span> Iniciar Gravação
      </button>
      <button
          :class="{ 'disabled': !conectado || !recorder }"
          :disabled="!conectado || !recorder"
          class="btn btn-danger"
          @click="pararGravacao"
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
import {computed, onMounted, onUnmounted, ref} from 'vue'
import WebSocketService from '../services/WebSocketService'

const recorder = ref(null)
const transcricao = ref('Aguardando transcrição...')
const resumo = ref('Aguardando resumo...')
const extrato = ref('Aguardando extrato...')

// Get the connection status from the WebSocket service
const conectado = computed(() => WebSocketService.connected)

// Get the client UUID from the WebSocket service
const clientUuid = computed(() => WebSocketService.clientUuid)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to user-specific destinations
  subscriptions.push(
      WebSocketService.subscribe('/topic/transcricao/resultado', msg => {
        try {
          const data = JSON.parse(msg.body)
          processarRetorno(data)
        } catch (err) {
          console.error('Error parsing message', err, msg.body)
        }
      })
  )

  subscriptions.push(
      WebSocketService.subscribe('/topic/transcricao/error', msg => {
        console.error('Error from server:', msg.body)
      })
  )


  // Send initialization message
  WebSocketService.publish("/app/transcricao/iniciar")
})

const iniciarGravacao = async () => {
  navigator.mediaDevices.getUserMedia({audio: true})
      .then(stream => {
        let indice = 1
        recorder.value = new MediaRecorder(stream)
        recorder.value.ondataavailable = event => event.data.arrayBuffer().then(buffer => enviarAudio(indice++, buffer))
        recorder.value.onstop = _ => finalizar()
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
  if (conectado.value) {
    try {
      let chunk = {
        indice,
        base64: btoa(String.fromCharCode(...new Uint8Array(audio)))
      }
      WebSocketService.publish("/app/transcricao/chunk", chunk)
    } catch (err) {
      console.error('Error sending audio chunk', err)
    }
  } else {
    console.warn('Cannot send audio: WebSocket is not connected')
  }
}

const finalizar = () => {
  if (conectado.value) {
    try {
      WebSocketService.publish("/app/transcricao/finalizar")
    } catch (err) {
      console.error('Error sending finalizar message', err)
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

  // Unsubscribe from all subscriptions
  subscriptions.forEach(subscription => {
    if (subscription) {
      try {
        subscription.unsubscribe()
      } catch (err) {
        console.error('Error unsubscribing', err)
      }
    }
  })
  subscriptions = []
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

.client-id {
  font-size: 12px;
  color: #666;
  background-color: #f0f0f0;
  padding: 4px 8px;
  border-radius: 4px;
  margin-top: 5px;
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

  .controls {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }
}
</style>
