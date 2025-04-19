<template>
  <div>
    <h3>🎙️ Tanscrição de Áudio - {{ conectado ? 'Conectado' : 'Desconectado' }}</h3>
    <button @click="iniciarGravacao" :disabled="!conectado || recorder">Iniciar Gravação</button>
    <button @click="pararGravacao" :disabled="!conectado || !recorder">Parar Gravação</button>
    <div class="transcricao">
      <h2>🧠 Transcrição</h2>
      <pre>{{ transcricao }}</pre>
    </div>
    <div class="transcricao">
      <h2>🧠 Resumo</h2>
      <pre>{{ resumo }}</pre>
    </div>
  </div>
</template>

<script setup>
import {onUnmounted, ref} from 'vue'
import SockJS from 'sockjs-client'

const conectado = ref(false)
const recorder = ref(null)
const transcricao = ref('Aguardando transcrição...')
const resumo = ref('Aguardando resumo...')

const socket = new SockJS('/ws-transcricao')
socket.onopen = () => habilitar()
socket.onmessage = e => processarRetorno(JSON.parse(e.data))
socket.onclose = () => desabilitar()
socket.onerror = err => console.error('Socket error', err)

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
  }
}

const pararGravacao = () => {
  if (recorder.value && recorder.value.state === 'recording')
    recorder.value.stop()
}

const enviarAudio = (indice, audio) => {
  if (socket && conectado.value) {
    let chunk = {
      indice,
      base64: btoa(String.fromCharCode(...new Uint8Array(audio)))
    }
    socket.send(JSON.stringify(chunk))
  }
}

const finalizar = () => {
  if (socket && conectado.value)
    socket.send('FIM')
  recorder.value = null;
}

onUnmounted(() => {
  if (recorder.value?.state === 'recording')
    recorder.value.stop()
})

</script>