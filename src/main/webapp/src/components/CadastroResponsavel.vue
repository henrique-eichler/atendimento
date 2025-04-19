
<template>
  <div>
    <h2>Cadastro de Responsável</h2>
    <input v-model="nome" placeholder="Nome do Responsável" />
    <button @click="cadastrar">Cadastrar</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWebSocket } from '../websocket'

const nome = ref('')
const { sendMessage } = useWebSocket()

const cadastrar = () => {
  sendMessage('/app/cadastrar-responsavel', { nome: nome.value })
  nome.value = ''
}
</script>
