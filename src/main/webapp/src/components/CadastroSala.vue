<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Sala</h1>
    </div>

    <div class="form-container" v-if="isEditing">
      <div class="form-card">
        <h2>✏️ Formulário</h2>
        <div class="form-content">
          <div class="form-group">
            <label>Número da Sala</label>
            <input v-model="numero" placeholder="Número da Sala" required type="number"/>
          </div>
          <div class="form-group">
            <label>Terapias</label>
            <select v-model="selectedTerapias" multiple class="multi-select">
              <option v-for="terapia in terapias" :key="terapia.id" :value="terapia">
                {{ terapia.nome }}
              </option>
            </select>
            <div class="selected-items">
              <div v-for="terapia in selectedTerapias" :key="terapia.id" class="selected-item">
                {{ terapia.nome }}
                <button @click="removeTerapia(terapia)" class="remove-btn">×</button>
              </div>
            </div>
          </div>
          <div class="controls">
            <button class="btn btn-primary" @click="salvar()">
              <span class="icon">💾</span> Salvar
            </button>
            <button class="btn btn-danger" @click="cancelar()">
              <span class="icon">❌</span> Cancelar
            </button>
          </div>
        </div>
      </div>
    </div>

    <div class="results-container">
      <div class="result-card">
        <h2>📋 Lista de Salas</h2>
        <div class="content">
          <div class="table-controls">
            <button class="btn btn-primary" @click="novo()">
              <span class="icon">📄</span> Nova Sala
            </button>
          </div>
          <table class="data-table">
            <thead>
              <tr>
                <th>Número</th>
                <th>Terapias</th>
                <th>Ações</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="sala in salas" :key="sala.id">
                <td>{{ sala.numero }}</td>
                <td>
                  <div class="terapias-list">
                    <span v-if="!sala.terapias || sala.terapias.length === 0" class="no-terapias">Nenhuma terapia associada</span>
                    <div v-else class="terapia-tag" v-for="terapia in sala.terapias" :key="terapia.id">
                      {{ terapia.nome }}
                    </div>
                  </div>
                </td>
                <td class="actions">
                  <button class="btn-icon" @click="editar(sala)">
                    <span class="icon">📝</span>
                  </button>
                  <button class="btn-icon" @click="remover(sala.id)">
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
import {onMounted, onUnmounted, ref} from "vue"
import SockJS from "sockjs-client"
import {Client} from "@stomp/stompjs"

const salas = ref([])
const terapias = ref([])
const selectedTerapias = ref([])
const isEditing = ref(false)
const id = ref(null)
const numero = ref("")

const stomp = new Client({
  webSocketFactory: () => new SockJS("/ws-cadastro"),
  reconnectDelay: 5000,
})

stomp.onConnect = () => {
  stomp.subscribe("/topic/sala/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/sala/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/sala/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  stomp.subscribe("/topic/sala/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))
  stomp.subscribe("/topic/terapia/retorno/listar", msg => retornoListarTerapias(JSON.parse(msg.body)))

  stomp.publish({destination: "/app/sala/listar"})
  stomp.publish({destination: "/app/terapia/listar"})
}

const retornoListar = lista => {
  salas.value = lista
}

const retornoSalvar = sala => {
  salas.value.push(sala)
}

const retornoEditar = sala => {
  const i = salas.value.findIndex(p => p.id === sala.id)
  if (i >= 0) salas.value.splice(i, 1, sala)
}

const retornoExcluir = id => {
  salas.value = salas.value.filter(p => p.id !== id)
}

const retornoListarTerapias = lista => {
  terapias.value = lista
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = sala => {
  id.value = sala.id;
  numero.value = sala.numero;
  // If the sala has terapias property, load it
  selectedTerapias.value = sala.terapias || [];
  isEditing.value = true;
}

const salvar = () => {
  const salaData = {
    id: id.value,
    numero: parseInt(numero.value),
    terapias: selectedTerapias.value
  };
  stomp.publish({
    destination: "/app/sala/salvar",
    body: JSON.stringify(salaData)
  })
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    stomp.publish({
      destination: "/app/sala/excluir",
      body: JSON.stringify(idToRemove)
    })
  }
}

const cancelar = () => {
  reset()
}

const removeTerapia = (terapia) => {
  selectedTerapias.value = selectedTerapias.value.filter(t => t.id !== terapia.id);
}

const reset = () => {
  isEditing.value = false
  id.value = null;
  numero.value = "";
  selectedTerapias.value = [];
}

onMounted(() => {
  stomp.activate()
})

onUnmounted(() => {
  stomp.deactivate()
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

.form-container {
  margin-bottom: 25px;
}

.form-card {
  background-color: white;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.form-card h2 {
  font-size: 18px;
  margin-top: 0;
  margin-bottom: 15px;
  color: #2c3e50;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
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

.controls {
  display: flex;
  gap: 15px;
  margin-top: 20px;
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
  margin-top: 0;
  margin-bottom: 15px;
  color: #2c3e50;
  border-bottom: 2px solid #e0e0e0;
  padding-bottom: 10px;
}

.content {
  font-size: 15px;
  line-height: 1.5;
  color: #555;
}

.table-controls {
  margin-bottom: 15px;
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

.multi-select {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  min-height: 100px;
}

.selected-items {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}

.selected-item {
  display: flex;
  align-items: center;
  background-color: #e3f2fd;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 14px;
}

.remove-btn {
  background: none;
  border: none;
  color: #f44336;
  font-size: 16px;
  font-weight: bold;
  cursor: pointer;
  margin-left: 5px;
}

.remove-btn:hover {
  color: #d32f2f;
}

.terapias-list {
  display: flex;
  flex-wrap: wrap;
  gap: 5px;
}

.terapia-tag {
  background-color: #e3f2fd;
  padding: 3px 8px;
  border-radius: 12px;
  font-size: 12px;
  white-space: nowrap;
}

.no-terapias {
  color: #999;
  font-style: italic;
  font-size: 12px;
}

@media (max-width: 768px) {
  .controls {
    flex-direction: column;
  }

  .btn {
    width: 100%;
  }

  .data-table {
    display: block;
    overflow-x: auto;
  }
}
</style>
