<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Sala</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Sala" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Id</label>
          <input v-model="sala.id" placeholder="Id da Sala" readonly type="number"/>
        </div>
        <div class="form-group">
          <label>Número</label>
          <input v-model="sala.numero" placeholder="Número da Sala" required type="number"/>
        </div>

        <!-- Tab Navigation -->
        <div class="tab-navigation">
          <button :class="{ active: activeTab === 'terapias' }" class="tab-button" @click="activeTab = 'terapias'">Terapias</button>
          <button :class="{ active: activeTab === 'recursos' }" class="tab-button" @click="activeTab = 'recursos'">Recursos</button>
          <button :class="{ active: activeTab === 'cronograma' }" class="tab-button" @click="activeTab = 'cronograma'">Cronograma</button>
        </div>

        <!-- Tab Content -->
        <div class="tab-content">
          <!-- Terapias Tab -->
          <div v-if="activeTab === 'terapias'" class="tab-pane">
            <div class="form-group">
              <label>Terapias</label>
              <div class="dual-list-container">

                <div class="list-box">
                  <div class="list-header">
                    <h3>Selecionadas</h3>
                    <input v-model="search.terapias.selecionadas" class="search-input" placeholder="Buscar..."/>
                  </div>
                  <div ref="selectedListRef" class="list-content">
                    <div v-for="terapia in terapiasSelecionadas" :key="terapia.id" class="list-item" @click="sala.terapias = sala.terapias.filter(t => t.id !== terapia.id)">{{ terapia.nome }}</div>
                  </div>
                </div>

                <div class="list-box">
                  <div class="list-header">
                    <h3>Disponíveis</h3>
                    <input v-model="search.terapias.disponiveis" class="search-input" placeholder="Buscar..."/>
                  </div>
                  <div ref="availableListRef" class="list-content">
                    <div v-for="terapia in terapiasDisponiveis" :key="terapia.id" class="list-item" @click="sala.terapias.push(terapia)">{{ terapia.nome }}</div>
                  </div>
                </div>

              </div>
            </div>
          </div>

          <!-- Recursos Tab -->
          <div v-if="activeTab === 'recursos'" class="tab-pane">
            <div class="form-group">
              <label>Recursos</label>
              <div class="dual-list-container">

                <div class="list-box">
                  <div class="list-header">
                    <h3>Selecionados</h3>
                    <input v-model="search.recursos.selecionados" class="search-input" placeholder="Buscar..."/>
                  </div>
                  <div ref="selectedListRef" class="list-content">
                    <div v-for="recurso in recursosSelecionados" :key="recurso.id" class="list-item" @click="sala.recursos = sala.recursos.filter(t => t.id !== recurso.id)">{{ recurso.nome }}</div>
                  </div>
                </div>

                <div class="list-box">
                  <div class="list-header">
                    <h3>Disponíveis</h3>
                    <input v-model="search.recursos.disponiveis" class="search-input" placeholder="Buscar..."/>
                  </div>
                  <div ref="availableListRef" class="list-content">
                    <div v-for="recurso in recursosDisponiveis" :key="recurso.id" class="list-item" @click="sala.recursos.push(recurso)">{{ recurso.nome }}</div>
                  </div>
                </div>

              </div>
            </div>
          </div>

          <!-- Cronograma Tab -->
          <div v-if="activeTab === 'cronograma'" class="tab-pane">
            <div class="form-group">
              <label>Cronograma</label>
              <div class="cronograma-grid-container">
                <table class="cronograma-grid">
                  <thead>
                    <tr>
                      <th>Horário</th>
                      <th v-for="dia in diasSemana" :key="dia.value">{{ dia.label }}</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="hora in horas" :key="hora.value">
                      <td>{{ hora.label }}</td>
                      <td v-for="dia in diasSemana" :key="dia.value" :class="{ 'selected': isCronogramaSelected(dia.value, hora.value) }" @click="toggleCronograma(dia.value, hora.value)"></td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
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
          <h2>📋 Lista de Salas</h2>
          <div class="card-buttons">
            <button class="btn btn-primary" @click="listarSalas()">
              <span class="icon">🔄</span> Atualizar
            </button>
            <button class="btn btn-primary" @click="novo()">
              <span class="icon">📄</span> Nova Sala
            </button>
          </div>
        </div>
        <div class="content">
          <table class="data-table">
            <thead>
            <tr>
              <th>Número</th>
              <th>Terapias</th>
              <th>Recursos</th>
              <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="sala in salas" :key="sala.id">
              <td>{{ sala.numero }}</td>
              <td>
                <div class="terapias-list">
                  <span v-if="!sala.terapias || sala.terapias.length === 0" class="no-terapias">Nenhuma terapia associada</span>
                  <div v-for="terapia in sala.terapias" v-else :key="terapia.id" class="terapia-tag">{{ terapia.nome }}</div>
                </div>
              </td>
              <td>
                <div class="recursos-list">
                  <span v-if="!sala.recursos || sala.recursos.length === 0" class="no-recursos">Nenhum recurso associado</span>
                  <div v-for="recurso in sala.recursos" v-else :key="recurso.id" class="recurso-tag">{{ recurso.nome }}</div>
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
import {computed, onMounted, onUnmounted, ref} from "vue"
import WebSocketService from '../services/WebSocketService'
import ModalForm from './ModalForm.vue'

const salas = ref([])
const sala = ref({id: null, numero: null, terapias: [], recursos: [], cronogramas: []})
const isEditing = ref(false)

const activeTab = ref("terapias")
const terapias = ref([])
const recursos = ref([])

// Cronograma data
const diasSemana = [
  { value: 'DOMINGO', label: 'Domingo' },
  { value: 'SEGUNDA', label: 'Segunda' },
  { value: 'TERCA', label: 'Terça' },
  { value: 'QUARTA', label: 'Quarta' },
  { value: 'QUINTA', label: 'Quinta' },
  { value: 'SEXTA', label: 'Sexta' },
  { value: 'SABADO', label: 'Sábado' }
]

const horas = []
// Generate hours from 7:00 AM to 7:00 PM
for (let i = 7; i <= 19; i++) {
  horas.push({
    value: i,
    label: `${i}:00`
  })
}

const search = ref({terapias: {disponiveis: "", selecionadas: ""}, recursos:{disponiveis: "", selecionados: ""}})
const terapiasDisponiveis = computed(() => terapias.value.filter(t => t.nome.toLowerCase().includes(search.value.terapias.disponiveis) && !sala.value.terapias.some(s => s.id === t.id)))
const terapiasSelecionadas = computed(() => terapias.value.filter(t => t.nome.toLowerCase().includes(search.value.terapias.selecionadas) && sala.value.terapias.some(s => s.id === t.id)))
const recursosDisponiveis = computed(() => recursos.value.filter(t => t.nome.toLowerCase().includes(search.value.recursos.disponiveis) && !sala.value.recursos.some(s => s.id === t.id)))
const recursosSelecionados = computed(() => recursos.value.filter(t => t.nome.toLowerCase().includes(search.value.recursos.selecionados) && sala.value.recursos.some(s => s.id === t.id)))

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(WebSocketService.subscribe("/topic/sala/response/save", msg => saved(msg.body)))
  subscriptions.push(WebSocketService.subscribe("/topic/sala/response/delete", msg => deleted(msg.body)))
  subscriptions.push(WebSocketService.subscribe("/topic/sala/response/list", msg => salas.value = msg.body))
  subscriptions.push(WebSocketService.subscribe("/topic/terapia/response/list", msg => terapias.value = msg.body))
  subscriptions.push(WebSocketService.subscribe("/topic/recurso/response/list", msg => recursos.value = msg.body))

  // Send initialization messages
  WebSocketService.publish("/topic/sala/request/list")
  WebSocketService.publish("/topic/terapia/request/list")
  WebSocketService.publish("/topic/recurso/request/list")
})

const listarSalas = () => {
  WebSocketService.publish("/topic/sala/request/list")
}

const saved = sala => {
  const index = salas.value.findIndex(t => t.id === sala.id);
  if (index !== -1) {
    salas.value[index] = sala; // Replace
  } else {
    salas.value.push(sala);    // Add
  }
}

const deleted = id => {
  salas.value = salas.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = s => {
  sala.value.id = s.id;
  sala.value.numero = s.numero;
  sala.value.terapias = [...s.terapias]
  sala.value.recursos = [...s.recursos]
  sala.value.cronogramas = s.cronogramas ? [...s.cronogramas] : []
  isEditing.value = true;
  activeTab.value = "cadastro"
}

const salvar = () => {
  WebSocketService.publish("/topic/sala/request/save", sala.value)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/topic/sala/request/delete", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

// Cronograma methods
const isCronogramaSelected = (diaSemana, hora) => {
  return sala.value.cronogramas.some(c => 
    c.diaSemana === diaSemana && 
    new Date(c.horaInicio).getHours() === hora
  );
}

const toggleCronograma = (diaSemana, hora) => {
  // Check if this time slot is already selected
  const existingIndex = sala.value.cronogramas.findIndex(c => 
    c.diaSemana === diaSemana && 
    new Date(c.horaInicio).getHours() === hora
  );

  if (existingIndex !== -1) {
    // If already selected, remove it
    sala.value.cronogramas.splice(existingIndex, 1);
  } else {
    // If not selected, add it
    const horaInicio = new Date();
    horaInicio.setHours(hora, 0, 0, 0);

    const horaTermino = new Date();
    horaTermino.setHours(hora + 1, 0, 0, 0);

    sala.value.cronogramas.push({
      id: null,
      sala: { id: sala.value.id },
      diaSemana: diaSemana,
      horaInicio: horaInicio.toISOString(),
      horaTermino: horaTermino.toISOString()
    });
  }
}

const reset = () => {
  isEditing.value = false
  sala.value.id = null
  sala.value.numero = null
  sala.value.terapias = []
  sala.value.recursos = []
  sala.value.cronogramas = []
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

/* Cronograma Grid Styles */
.cronograma-grid-container {
  margin-top: 10px;
  overflow-x: auto;
}

.cronograma-grid {
  width: 100%;
  border-collapse: collapse;
  border: 1px solid #ddd;
}

.cronograma-grid th,
.cronograma-grid td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.cronograma-grid th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.cronograma-grid td:not(:first-child) {
  cursor: pointer;
  width: 60px;
  height: 30px;
}

.cronograma-grid td:not(:first-child):hover {
  background-color: #f5f5f5;
}

.cronograma-grid td.selected {
  background-color: #4CAF50;
  color: white;
}

.cronograma-grid td.selected:hover {
  background-color: #45a049;
}
</style>
