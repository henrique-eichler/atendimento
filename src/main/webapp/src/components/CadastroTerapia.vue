<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Terapia</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Terapia" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Nome da Terapia</label>
          <input v-model="nome" placeholder="Nome da Terapia" required/>
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
          <h2>📋 Lista de Terapias</h2>
          <div class="card-buttons">
            <button class="btn btn-primary" @click="listarTerapias()">
              <span class="icon">🔄</span> Atualizar
            </button>
            <button class="btn btn-primary" @click="novo()">
              <span class="icon">📄</span> Nova Terapia
            </button>
          </div>
        </div>
        <div class="content">
          <table class="data-table">
            <thead>
              <tr>
                <th>Nome</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="terapia in terapias" :key="terapia.id">
                <td>{{ terapia.nome }}</td>
                <td class="actions">
                  <button class="btn-icon" @click="editar(terapia)">
                    <span class="icon">📝</span>
                  </button>
                  <button class="btn-icon" @click="remover(terapia.id)">
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

const terapias = ref([])
const isEditing = ref(false)
const id = ref(null)
const nome = ref("")

// Get the connection status from the WebSocket service
const conectado = computed(() => WebSocketService.connected)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
    WebSocketService.subscribe("/topic/terapia/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/terapia/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/terapia/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/terapia/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))
  )

  // Send initialization message
  WebSocketService.publish("/app/terapia/listar")
})

const retornoListar = lista => {
  terapias.value = lista
}

const listarTerapias = () => {
  WebSocketService.publish("/app/terapia/listar")
}

const retornoSalvar = terapia => {
  terapias.value.push(terapia)
}

const retornoEditar = terapia => {
  const i = terapias.value.findIndex(p => p.id === terapia.id)
  if (i >= 0) terapias.value.splice(i, 1, terapia)
}

const retornoExcluir = id => {
  terapias.value = terapias.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = terapia => {
  id.value = terapia.id;
  nome.value = terapia.nome;
  isEditing.value = true;
}

const salvar = () => {
  const terapiaData = {
    id: id.value,
    nome: nome.value
  };
  WebSocketService.publish("/app/terapia/salvar", terapiaData)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/app/terapia/excluir", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  id.value = null;
  nome.value = "";
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
