<template>
  <div>
    <h2>Cadastro de Paciente</h2>

    <form v-if="isEditing">
      <input v-model="form.nome" placeholder="Nome" required/>
      <input v-model="form.dataNascimento" type="date" required/>
      <select v-model="form.sexo" required>
        <option disabled value="">Sexo</option>
        <option value="MASCULINO">M</option>
        <option value="FEMININO">F</option>
      </select>
      <button @click="salvar()">Salvar</button>
      <button @click="cancelar()">Cancelar</button>
    </form>

    <table>
      <thead>
      <tr>
        <th>Nome</th>
        <th>Nasc.</th>
        <th>Sexo</th>
        <th>
          <button @click="novo()">📄</button>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="paciente in pacientes" :key="paciente.id">
        <td>{{ paciente.nome }}</td>
        <td>{{ paciente.dataNascimento }}</td>
        <td>{{ paciente.sexo }}</td>
        <td>
          <button @click="editar(paciente)">📝</button>
          <button @click="remover(paciente.id)">🗑️</button>
        </td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, reactive, ref} from "vue"
import SockJS from "sockjs-client"
import {Client} from "@stomp/stompjs"

const pacientes = ref([])
const isEditing = ref(false)
const form = reactive({id: null, nome: "", dataNascimento: "", sexo: ""})

const stomp = new Client({
  webSocketFactory: () => new SockJS("/ws-cadastro"),
  reconnectDelay: 5000,
})

stomp.onConnect = () => {
  stomp.subscribe("/topic/paciente/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/paciente/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/paciente/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/paciente/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))

  stomp.publish({destination: "/app/paciente/listar"})
}

const retornoListar = lista => {
  pacientes.value = lista
}

const retornoSalvar = paciente => {
  pacientes.value.push(paciente)
}

const retornoEditar = paciente => {
  const i = pacientes.value.findIndex(p => p.id === paciente.id)
  if (i >= 0) pacientes.value.splice(i, 1, paciente)
}

const retornoExcluir = id => {
  pacientes.value = pacientes.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = paciente => {
  Object.assign(form, paciente);
  isEditing.value = true;
}

const salvar = () => {
  stomp.publish({
    destination: "/app/paciente/salvar",
    body: JSON.stringify(form)
  })
  reset();
}

const remover = id => {
  if (confirm("Excluir?")) {
    stomp.publish({
      destination: "/app/paciente/excluir",
      body: JSON.stringify(id)
    })
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  form.id = form.nome = form.dataNascimento = form.sexo = null
}

onMounted(() => {
  stomp.activate()
})

onUnmounted(() => {
  stomp.deactivate()
})

</script>