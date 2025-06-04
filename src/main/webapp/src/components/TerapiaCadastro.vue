<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Terapia</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Terapia" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Id</label>
          <input v-model="terapia.id" placeholder="Id da Terapia" readonly/>
        </div>
        <div class="form-group">
          <label>Nome</label>
          <input v-model="terapia.nome" placeholder="Nome da Terapia" required/>
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
import {computed, onMounted, onUnmounted, ref} from "vue"
import WebSocketService from '../services/WebSocketService'
import ModalForm from './ModalForm.vue'

const terapias = ref([])
const terapia = ref({id: null, nome: ""})
const isEditing = ref(false)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
      WebSocketService.subscribe("/topic/terapia/response/list", msg => listed(msg.body))
  )

  subscriptions.push(
      WebSocketService.subscribe("/topic/terapia/response/save", msg => saved(msg.body))
  )

  subscriptions.push(
      WebSocketService.subscribe("/topic/terapia/response/delete", msg => deleted(msg.body))
  )

  // Send initialization message
  WebSocketService.publish("/topic/terapia/request/list")
})

const listarTerapias = () => {
  WebSocketService.publish("/topic/terapia/request/list")
}

const listed = list => {
  terapias.value = list
}

const saved = terapia => {
  const index = terapias.value.findIndex(t => t.id === terapia.id);
  if (index !== -1) {
    terapias.value[index] = terapia; // Replace
  } else {
    terapias.value.push(terapia);    // Add
  }
}

const deleted = id => {
  terapias.value = terapias.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = t => {
  terapia.value.id = t.id;
  terapia.value.nome = t.nome;
  isEditing.value = true;
}

const salvar = () => {
  WebSocketService.publish("/topic/terapia/request/save", terapia.value)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/topic/terapia/request/delete", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  terapia.value.id = null;
  terapia.value.nome = "";
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
