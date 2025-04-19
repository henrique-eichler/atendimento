<template>
  <div>
    <h2>Cadastro de Profissão</h2>

    <form v-if="isEditing">
      <input v-model="form.nome" placeholder="Nome" required/>
      <button @click="salvar()">Salvar</button>
      <button @click="cancelar()">Cancelar</button>
    </form>

    <table>
      <thead>
      <tr>
        <th>Nome</th>
        <th>
          <button @click="novo()">📄</button>
        </th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="profissao in profissoes" :key="profissao.id">
        <td>{{ profissao.nome }}</td>
        <td>
          <button @click="editar(profissao)">📝</button>
          <button @click="remover(profissao.id)">🗑️</button>
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

const profissoes = ref([])
const isEditing = ref(false)
const form = reactive({id: null, nome: ""})

const stomp = new Client({
  webSocketFactory: () => new SockJS("/ws-cadastro"),
  reconnectDelay: 5000,
})

stomp.onConnect = () => {
  stomp.subscribe("/topic/profissao/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/profissao/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/profissao/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/profissao/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))

  stomp.publish({destination: "/app/profissao/listar"})
}

const retornoListar = lista => {
  profissoes.value = lista
}

const retornoSalvar = profissao => {
  profissoes.value.push(profissao)
}

const retornoEditar = profissao => {
  const i = profissoes.value.findIndex(p => p.id === profissao.id)
  if (i >= 0) profissoes.value.splice(i, 1, profissao)
}

const retornoExcluir = id => {
  profissoes.value = profissoes.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = profissao => {
  Object.assign(form, profissao);
  isEditing.value = true;
}

const salvar = () => {
  stomp.publish({
    destination: "/app/profissao/salvar",
    body: JSON.stringify(form)
  })
  reset();
}

const remover = id => {
  if (confirm("Excluir?")) {
    stomp.publish({
      destination: "/app/profissao/excluir",
      body: JSON.stringify(id)
    })
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  form.id = form.nome = null
}

onMounted(() => {
  stomp.activate()
})

onUnmounted(() => {
  stomp.deactivate()
})

</script>