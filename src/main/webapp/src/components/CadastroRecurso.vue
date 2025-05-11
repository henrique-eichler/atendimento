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
          <button class="btn btn-danger" @click="cancelar()">
            <span class="icon">❌</span> Cancelar
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
    WebSocketService.subscribe("/topic/recurso/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/recurso/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/recurso/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/recurso/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))
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
.cadastro-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  font-family: 'Arial', sans-serif;
  color: #333;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #e0e0e0;
}

.header h1 {
  font-size: 24px;
  margin: 0;
  color: #2c3e50;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.card-header h2 {
  margin: 0;
  padding: 0;
  border-bottom: none;
}

.card-buttons {
  display: flex;
  gap: 10px;
}

.form-card h2 {
  font-size: 18px;
  color: #2c3e50;
  margin: 0;
  padding: 0;
  border-bottom: none;
}

.form-card-buttons {
  display: flex;
  gap: 10px;
}

.form-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
  color: #555;
}

.form-group input,
.form-group select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}


.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: all 0.3s ease;
}

.btn .icon {
  margin-right: 8px;
}

.btn-primary {
  background-color: #2196f3;
  color: white;
}

.btn-primary:hover {
  background-color: #0d8bf2;
}

.btn-danger {
  background-color: #f44336;
  color: white;
}

.btn-danger:hover {
  background-color: #e53935;
}

.results-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 20px;
}

.result-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.result-card h2 {
  font-size: 18px;
  color: #2c3e50;
}

.content {
  font-size: 15px;
  line-height: 1.5;
  color: #555;
}


.data-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
}

.data-table th,
.data-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.data-table th {
  background-color: #f5f5f5;
  font-weight: bold;
  color: #333;
}

.data-table tr:hover {
  background-color: #f9f9f9;
}

.actions {
  display: flex;
  gap: 5px;
}

.btn-icon {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 16px;
  padding: 5px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.btn-icon:hover {
  background-color: #f0f0f0;
}

@media (max-width: 768px) {
  .btn {
    width: 100%;
  }

  .data-table {
    display: block;
    overflow-x: auto;
  }
}
</style>
