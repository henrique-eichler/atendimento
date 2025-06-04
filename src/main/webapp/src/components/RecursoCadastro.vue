<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Recurso</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Recurso" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Id</label>
          <input v-model="recurso.id" placeholder="Nome do Recurso" readonly/>
        </div>
        <div class="form-group">
          <label>Nome</label>
          <input v-model="recurso.nome" placeholder="Nome do Recurso" required/>
        </div>
        <div class="form-group">
          <label>Descrição</label>
          <input v-model="recurso.descricao" placeholder="Descrição do Recurso"/>
        </div>
        <div class="form-group">
          <label>Número de Propriedade</label>
          <input v-model="recurso.numeroPropriedade" placeholder="Número de Propriedade" required type="number"/>
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
import {computed, onMounted, onUnmounted, ref} from "vue"
import WebSocketService from '../services/WebSocketService'
import ModalForm from './ModalForm.vue'

const recursos = ref([])
const recurso = ref({id: null, nome: "", descricao: "", numeroPropriedade: null})
const isEditing = ref(false)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
      WebSocketService.subscribe("/topic/recurso/response/list", msg => listed(msg.body))
  )

  subscriptions.push(
      WebSocketService.subscribe("/topic/recurso/response/save", msg => saved(msg.body))
  )

  subscriptions.push(
      WebSocketService.subscribe("/topic/recurso/response/deleted", msg => deleted(msg.body))
  )

  // Send initialization message
  WebSocketService.publish("/topic/recurso/request/list")
})

const listarRecursos = () => {
  WebSocketService.publish("/topic/recurso/request/list")
}

const listed = list => {
  recursos.value = list
}

const saved = recurso => {
  const index = recursos.value.findIndex(t => t.id === recurso.id);
  if (index !== -1) {
    recursos.value[index] = recurso; // Replace
  } else {
    recursos.value.push(recurso);    // Add
  }
}

const deleted = id => {
  recursos.value = recursos.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = r => {
  recurso.value.id = r.id;
  recurso.value.nome = r.nome;
  recurso.value.descricao = r.descricao;
  recurso.value.numeroPropriedade = r.numeroPropriedade;
  isEditing.value = true;
}

const salvar = () => {
  WebSocketService.publish("/topic/recurso/request/save", recurso.value)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/topic/recurso/request/delete", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  recurso.value.id = null;
  recurso.value.nome = "";
  recurso.value.descricao = "";
  recurso.value.numeroPropriedade = null;
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
