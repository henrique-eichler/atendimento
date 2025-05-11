<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Recurso</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Recurso" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Nome do Recurso</label>
          <input v-model="nome" placeholder="Nome do Recurso" required/>
        </div>
        <div class="form-group">
          <label>Descrição</label>
          <input v-model="descricao" placeholder="Descrição do Recurso"/>
        </div>
        <div class="form-group">
          <label>Número de Propriedade</label>
          <input v-model="numeroPropriedade" type="number" placeholder="Número de Propriedade" required/>
        </div>
      </div>
      <template #footer>
        <div class="form-card-buttons">
          <button class="btn btn-primary" @click="salvar()">
            <span class="icon">💾</span> Salvar
          </button>
        </div>
      </template>
    </ModalForm>

    <div class="results-container">
      <div class="result-card">
        <div class="card-header">
          <h2>📋 Lista de Recursos</h2>
          <div class="card-buttons">
            <button class="btn btn-primary" @click="listarRecursos()">
              <span class="icon">🔄</span> Atualizar
            </button>
            <button class="btn btn-primary" @click="novo()">
              <span class="icon">📄</span> Novo Recurso
            </button>
          </div>
        </div>
        <div class="content">
          <table class="data-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Descrição</th>
                <th>Número de Propriedade</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="recurso in recursos" :key="recurso.id">
                <td>{{ recurso.nome }}</td>
                <td>{{ recurso.descricao }}</td>
                <td>{{ recurso.numeroPropriedade }}</td>
                <td class="actions">
                  <button class="btn-icon" @click="editar(recurso)">
                    <span class="icon">📝</span>
                  </button>
                  <button class="btn-icon" @click="remover(recurso.id)">
                    <span class="icon">🗑️</span>
                  </button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, computed} from "vue"
import WebSocketService from '../services/WebSocketService'
import ModalForm from './ModalForm.vue'

const recursos = ref([])
const isEditing = ref(false)
const id = ref(null)
const nome = ref("")
const descricao = ref("")
const numeroPropriedade = ref(null)

// Get the connection status from the WebSocket service
const conectado = computed(() => WebSocketService.connected)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
    WebSocketService.subscribe("/app/recurso/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/app/recurso/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/app/recurso/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/app/recurso/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))
  )

  // Send initialization message
  WebSocketService.publish("/app/recurso/listar")
})

const retornoListar = lista => {
  recursos.value = lista
}

const listarRecursos = () => {
  WebSocketService.publish("/app/recurso/listar")
}

const retornoSalvar = recurso => {
  recursos.value.push(recurso)
}

const retornoEditar = recurso => {
  const i = recursos.value.findIndex(p => p.id === recurso.id)
  if (i >= 0) recursos.value.splice(i, 1, recurso)
}

const retornoExcluir = id => {
  recursos.value = recursos.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = recurso => {
  id.value = recurso.id;
  nome.value = recurso.nome;
  descricao.value = recurso.descricao;
  numeroPropriedade.value = recurso.numeroPropriedade;
  isEditing.value = true;
}

const salvar = () => {
  const recursoData = {
    id: id.value,
    nome: nome.value,
    descricao: descricao.value,
    numeroPropriedade: numeroPropriedade.value
  };
  WebSocketService.publish("/app/recurso/salvar", recursoData)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/app/recurso/excluir", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  id.value = null;
  nome.value = "";
  descricao.value = "";
  numeroPropriedade.value = null;
}

onUnmounted(() => {
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
/* Component-specific styles would go here */
</style>
