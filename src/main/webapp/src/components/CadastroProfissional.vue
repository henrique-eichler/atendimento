
<template>
  <div>
    <h2>Cadastro de Profissional</h2>
    <input v-model="nome" placeholder="Nome do Profissional" />
    <input v-model="profissao" placeholder="Profissão" />
    <button @click="cadastrar">Cadastrar</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWebSocket } from '../websocket'

const nome = ref('')
const profissao = ref('')
const { sendMessage } = useWebSocket()

const cadastrar = () => {
  sendMessage('/app/cadastrar-profissional', {
    nome: nome.value,
    profissao: profissao.value
  })
  nome.value = ''
  profissao.value = ''
}
</script>
