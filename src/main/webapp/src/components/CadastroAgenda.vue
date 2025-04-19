
<template>
  <div>
    <h2>Cadastro de Agenda</h2>
    <input v-model="pacienteId" placeholder="ID do Paciente" />
    <input v-model="responsavelId" placeholder="ID do Responsável" />
    <input v-model="profissionalId" placeholder="ID do Profissional" />
    <input v-model="dataHora" placeholder="Data e Hora (YYYY-MM-DDTHH:MM)" />
    <button @click="cadastrar">Cadastrar</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useWebSocket } from '../websocket'

const pacienteId = ref('')
const responsavelId = ref('')
const profissionalId = ref('')
const dataHora = ref('')
const { sendMessage } = useWebSocket()

const cadastrar = () => {
  sendMessage('/app/cadastrar-agenda', {
    paciente: { id: pacienteId.value },
    responsavel: { id: responsavelId.value },
    profissional: { id: profissionalId.value },
    dataHora: dataHora.value
  })
}
</script>
