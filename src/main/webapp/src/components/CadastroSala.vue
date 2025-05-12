<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Sala</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Sala" @close="cancelar">
      <div class="form-content">
        <!-- Basic Information (Fixed) -->
        <div class="form-group">
          <label>Número da Sala</label>
          <input v-model="numero" placeholder="Número da Sala" required type="number"/>
        </div>

        <!-- Tab Navigation -->
        <div class="tab-navigation">
          <button 
            class="tab-button" 
            :class="{ active: activeTab === 'terapias' }" 
            @click="activeTab = 'terapias'"
          >
            Terapias
          </button>
          <button 
            class="tab-button" 
            :class="{ active: activeTab === 'recursos' }" 
            @click="activeTab = 'recursos'"
          >
            Recursos
          </button>
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
                      <h3>Terapias Disponíveis</h3>
                      <input 
                        v-model="availableTerapiaSearch" 
                        placeholder="Buscar..." 
                        class="search-input"
                        @input="filterAvailableTerapias"
                      />
                    </div>
                    <div class="list-content" ref="availableListRef">
                      <div 
                        v-for="terapia in filteredAvailableTerapias" 
                        :key="terapia.id" 
                        class="list-item"
                        draggable="true"
                        @dragstart="dragStart($event, terapia.id, 'available')"
                        @dragover.prevent
                        @drop="drop($event, 'available')"
                        @click="moveToSelected(terapia.id)"
                      >
                        {{ terapia.nome }}
                      </div>
                    </div>
                  </div>

                  <div class="list-controls">
                    <button 
                      class="control-btn" 
                      @click="moveAllToSelected()" 
                      :disabled="availableTerapias.length === 0"
                      title="Mover todos para selecionados"
                    >
                      ≫
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveSelectedToSelected()" 
                      :disabled="selectedAvailableIds.length === 0"
                      title="Mover selecionados para selecionados"
                    >
                      &gt;
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveSelectedToAvailable()" 
                      :disabled="selectedSelectedIds.length === 0"
                      title="Mover selecionados para disponíveis"
                    >
                      &lt;
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveAllToAvailable()" 
                      :disabled="selectedTerapiaIds.length === 0"
                      title="Mover todos para disponíveis"
                    >
                      ≪
                    </button>
                  </div>

                  <div class="list-box">
                    <div class="list-header">
                      <h3>Terapias Selecionadas</h3>
                      <input 
                        v-model="selectedTerapiaSearch" 
                        placeholder="Buscar..." 
                        class="search-input"
                        @input="filterSelectedTerapias"
                      />
                    </div>
                    <div class="list-content" ref="selectedListRef">
                      <div 
                        v-for="terapiaId in filteredSelectedTerapiaIds" 
                        :key="terapiaId" 
                        class="list-item"
                        draggable="true"
                        @dragstart="dragStart($event, terapiaId, 'selected')"
                        @dragover.prevent
                        @drop="drop($event, 'selected')"
                        @click="moveToAvailable(terapiaId)"
                      >
                        {{ getTerapiaNomeById(terapiaId) }}
                      </div>
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
                      <h3>Recursos Disponíveis</h3>
                      <input 
                        v-model="availableRecursoSearch" 
                        placeholder="Buscar..." 
                        class="search-input"
                        @input="filterAvailableRecursos"
                      />
                    </div>
                    <div class="list-content" ref="availableRecursosListRef">
                      <div 
                        v-for="recurso in filteredAvailableRecursos" 
                        :key="recurso.id" 
                        class="list-item"
                        draggable="true"
                        @dragstart="dragStartRecurso($event, recurso.id, 'availableRecurso')"
                        @dragover.prevent
                        @drop="dropRecurso($event, 'availableRecurso')"
                        @click="moveToSelectedRecurso(recurso.id)"
                      >
                        {{ recurso.nome }}
                      </div>
                    </div>
                  </div>

                  <div class="list-controls">
                    <button 
                      class="control-btn" 
                      @click="moveAllToSelectedRecurso()" 
                      :disabled="availableRecursos.length === 0"
                      title="Mover todos para selecionados"
                    >
                      ≫
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveSelectedToSelectedRecurso()" 
                      :disabled="selectedAvailableRecursoIds.length === 0"
                      title="Mover selecionados para selecionados"
                    >
                      &gt;
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveSelectedToAvailableRecurso()" 
                      :disabled="selectedSelectedRecursoIds.length === 0"
                      title="Mover selecionados para disponíveis"
                    >
                      &lt;
                    </button>
                    <button 
                      class="control-btn" 
                      @click="moveAllToAvailableRecurso()" 
                      :disabled="selectedRecursoIds.length === 0"
                      title="Mover todos para disponíveis"
                    >
                      ≪
                    </button>
                  </div>

                  <div class="list-box">
                    <div class="list-header">
                      <h3>Recursos Selecionados</h3>
                      <input 
                        v-model="selectedRecursoSearch" 
                        placeholder="Buscar..." 
                        class="search-input"
                        @input="filterSelectedRecursos"
                      />
                    </div>
                    <div class="list-content" ref="selectedRecursosListRef">
                      <div 
                        v-for="recursoId in filteredSelectedRecursoIds" 
                        :key="recursoId" 
                        class="list-item"
                        draggable="true"
                        @dragstart="dragStartRecurso($event, recursoId, 'selectedRecurso')"
                        @dragover.prevent
                        @drop="dropRecurso($event, 'selectedRecurso')"
                        @click="moveToAvailableRecurso(recursoId)"
                      >
                        {{ getRecursoNomeById(recursoId) }}
                      </div>
                    </div>
                  </div>
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
                    <div v-else class="terapia-tag" v-for="terapia in sala.terapias" :key="terapia.id">
                      {{ terapia.nome }}
                    </div>
                  </div>
                </td>
                <td>
                  <div class="recursos-list">
                    <span v-if="!sala.recursos || sala.recursos.length === 0" class="no-recursos">Nenhum recurso associado</span>
                    <div v-else class="recurso-tag" v-for="recurso in sala.recursos" :key="recurso.id">
                      {{ recurso.nome }}
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
import {onMounted, onUnmounted, ref, computed} from "vue"
import WebSocketService from '../services/WebSocketService'
import ModalForm from './ModalForm.vue'

const salas = ref([])
const terapias = ref([])
const recursos = ref([])
const selectedTerapiaIds = ref([])
const selectedRecursoIds = ref([])
const isEditing = ref(false)
const id = ref(null)
const numero = ref("")
const activeTab = ref("terapias")

// Refs for terapias dual-list selection
const availableTerapiaSearch = ref("")
const selectedTerapiaSearch = ref("")
const availableTerapias = ref([])
const filteredAvailableTerapias = ref([])
const filteredSelectedTerapiaIds = ref([])
const selectedAvailableIds = ref([])
const selectedSelectedIds = ref([])
const draggedItem = ref(null)
const draggedList = ref(null)
const availableListRef = ref(null)
const selectedListRef = ref(null)

// Refs for recursos dual-list selection
const availableRecursoSearch = ref("")
const selectedRecursoSearch = ref("")
const availableRecursos = ref([])
const filteredAvailableRecursos = ref([])
const filteredSelectedRecursoIds = ref([])
const selectedAvailableRecursoIds = ref([])
const selectedSelectedRecursoIds = ref([])
const draggedRecursoItem = ref(null)
const draggedRecursoList = ref(null)
const availableRecursosListRef = ref(null)
const selectedRecursosListRef = ref(null)

// Get the connection status from the WebSocket service
const conectado = computed(() => WebSocketService.connected)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
    WebSocketService.subscribe("/topic/sala/retorno/listar", msg => retornoListar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/sala/retorno/salvar", msg => retornoSalvar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/sala/retorno/editar", msg => retornoEditar(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/sala/retorno/excluir", msg => retornoExcluir(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/terapia/retorno/listar", msg => retornoListarTerapias(JSON.parse(msg.body)))
  )

  subscriptions.push(
    WebSocketService.subscribe("/topic/recurso/retorno/listar", msg => retornoListarRecursos(JSON.parse(msg.body)))
  )

  // Send initialization messages
  WebSocketService.publish("/app/sala/listar")
  WebSocketService.publish("/app/terapia/listar")
  WebSocketService.publish("/app/recurso/listar")
})

const retornoListar = lista => {
  salas.value = lista
}

const listarSalas = () => {
  WebSocketService.publish("/app/sala/listar")
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
  updateAvailableTerapias()
}

const retornoListarRecursos = lista => {
  recursos.value = lista
  updateAvailableRecursos()
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = sala => {
  id.value = sala.id;
  numero.value = sala.numero;
  // Load the sala's terapias IDs (backend now always includes this)
  selectedTerapiaIds.value = sala.terapias ? sala.terapias.map(terapia => terapia.id) : [];
  // Load the sala's recursos IDs
  selectedRecursoIds.value = sala.recursos ? sala.recursos.map(recurso => recurso.id) : [];
  updateAvailableTerapias();
  filterSelectedTerapias();
  updateAvailableRecursos();
  filterSelectedRecursos();
  isEditing.value = true;
}

const salvar = () => {
  // Convert terapia IDs to terapia objects with only id property
  const terapiaObjects = selectedTerapiaIds.value.map(id => ({ id }));

  // Convert recurso IDs to recurso objects with only id property
  const recursoObjects = selectedRecursoIds.value.map(id => ({ id }));

  const salaData = {
    id: id.value,
    numero: parseInt(numero.value),
    terapias: terapiaObjects,
    recursos: recursoObjects
  };
  WebSocketService.publish("/app/sala/salvar", salaData)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/app/sala/excluir", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const getTerapiaNomeById = (terapiaId) => {
  const terapia = terapias.value.find(t => t.id === terapiaId);
  return terapia ? terapia.nome : 'Terapia não encontrada';
}

const removeTerapiaById = (terapiaId) => {
  selectedTerapiaIds.value = selectedTerapiaIds.value.filter(id => id !== terapiaId);
}

const getRecursoNomeById = (recursoId) => {
  const recurso = recursos.value.find(r => r.id === recursoId);
  return recurso ? recurso.nome : 'Recurso não encontrado';
}

const removeRecursoById = (recursoId) => {
  selectedRecursoIds.value = selectedRecursoIds.value.filter(id => id !== recursoId);
}

// Update available terapias list (terapias not in selectedTerapiaIds)
const updateAvailableTerapias = () => {
  availableTerapias.value = terapias.value.filter(terapia => 
    !selectedTerapiaIds.value.includes(terapia.id)
  );
  filterAvailableTerapias();
}

// Filter available terapias based on search term
const filterAvailableTerapias = () => {
  if (!availableTerapiaSearch.value) {
    filteredAvailableTerapias.value = availableTerapias.value;
  } else {
    const searchTerm = availableTerapiaSearch.value.toLowerCase();
    filteredAvailableTerapias.value = availableTerapias.value.filter(terapia => 
      terapia.nome.toLowerCase().includes(searchTerm)
    );
  }
}

// Filter selected terapias based on search term
const filterSelectedTerapias = () => {
  if (!selectedTerapiaSearch.value) {
    filteredSelectedTerapiaIds.value = selectedTerapiaIds.value;
  } else {
    const searchTerm = selectedTerapiaSearch.value.toLowerCase();
    filteredSelectedTerapiaIds.value = selectedTerapiaIds.value.filter(terapiaId => {
      const terapia = terapias.value.find(t => t.id === terapiaId);
      return terapia && terapia.nome.toLowerCase().includes(searchTerm);
    });
  }
}

// Update available recursos list (recursos not in selectedRecursoIds)
const updateAvailableRecursos = () => {
  availableRecursos.value = recursos.value.filter(recurso => 
    !selectedRecursoIds.value.includes(recurso.id)
  );
  filterAvailableRecursos();
}

// Filter available recursos based on search term
const filterAvailableRecursos = () => {
  if (!availableRecursoSearch.value) {
    filteredAvailableRecursos.value = availableRecursos.value;
  } else {
    const searchTerm = availableRecursoSearch.value.toLowerCase();
    filteredAvailableRecursos.value = availableRecursos.value.filter(recurso => 
      recurso.nome.toLowerCase().includes(searchTerm)
    );
  }
}

// Filter selected recursos based on search term
const filterSelectedRecursos = () => {
  if (!selectedRecursoSearch.value) {
    filteredSelectedRecursoIds.value = selectedRecursoIds.value;
  } else {
    const searchTerm = selectedRecursoSearch.value.toLowerCase();
    filteredSelectedRecursoIds.value = selectedRecursoIds.value.filter(recursoId => {
      const recurso = recursos.value.find(r => r.id === recursoId);
      return recurso && recurso.nome.toLowerCase().includes(searchTerm);
    });
  }
}

// Move a terapia from available to selected
const moveToSelected = (terapiaId) => {
  if (!selectedTerapiaIds.value.includes(terapiaId)) {
    selectedTerapiaIds.value.push(terapiaId);
    updateAvailableTerapias();
    filterSelectedTerapias();
  }
}

// Move a terapia from selected to available
const moveToAvailable = (terapiaId) => {
  const index = selectedTerapiaIds.value.indexOf(terapiaId);
  if (index !== -1) {
    selectedTerapiaIds.value.splice(index, 1);
    updateAvailableTerapias();
    filterSelectedTerapias();
  }
}

// Move all available terapias to selected
const moveAllToSelected = () => {
  availableTerapias.value.forEach(terapia => {
    if (!selectedTerapiaIds.value.includes(terapia.id)) {
      selectedTerapiaIds.value.push(terapia.id);
    }
  });
  updateAvailableTerapias();
  filterSelectedTerapias();
}

// Move all selected terapias to available
const moveAllToAvailable = () => {
  selectedTerapiaIds.value = [];
  updateAvailableTerapias();
  filterSelectedTerapias();
}

// Move a recurso from available to selected
const moveToSelectedRecurso = (recursoId) => {
  if (!selectedRecursoIds.value.includes(recursoId)) {
    selectedRecursoIds.value.push(recursoId);
    updateAvailableRecursos();
    filterSelectedRecursos();
  }
}

// Move a recurso from selected to available
const moveToAvailableRecurso = (recursoId) => {
  const index = selectedRecursoIds.value.indexOf(recursoId);
  if (index !== -1) {
    selectedRecursoIds.value.splice(index, 1);
    updateAvailableRecursos();
    filterSelectedRecursos();
  }
}

// Move all available recursos to selected
const moveAllToSelectedRecurso = () => {
  availableRecursos.value.forEach(recurso => {
    if (!selectedRecursoIds.value.includes(recurso.id)) {
      selectedRecursoIds.value.push(recurso.id);
    }
  });
  updateAvailableRecursos();
  filterSelectedRecursos();
}

// Move all selected recursos to available
const moveAllToAvailableRecurso = () => {
  selectedRecursoIds.value = [];
  updateAvailableRecursos();
  filterSelectedRecursos();
}

// Move selected items from available to selected
const moveSelectedToSelected = () => {
  selectedAvailableIds.value.forEach(terapiaId => {
    if (!selectedTerapiaIds.value.includes(terapiaId)) {
      selectedTerapiaIds.value.push(terapiaId);
    }
  });
  selectedAvailableIds.value = [];
  updateAvailableTerapias();
  filterSelectedTerapias();
}

// Move selected items from selected to available
const moveSelectedToAvailable = () => {
  selectedSelectedIds.value.forEach(terapiaId => {
    const index = selectedTerapiaIds.value.indexOf(terapiaId);
    if (index !== -1) {
      selectedTerapiaIds.value.splice(index, 1);
    }
  });
  selectedSelectedIds.value = [];
  updateAvailableTerapias();
  filterSelectedTerapias();
}

// Handle drag start event
const dragStart = (event, id, listType) => {
  draggedItem.value = id;
  draggedList.value = listType;
  event.dataTransfer.effectAllowed = 'move';
}

// Handle drop event
const drop = (event, targetList) => {
  event.preventDefault();

  if (!draggedItem.value || draggedList.value === targetList) return;

  if (draggedList.value === 'available' && targetList === 'selected') {
    moveToSelected(draggedItem.value);
  } else if (draggedList.value === 'selected' && targetList === 'available') {
    moveToAvailable(draggedItem.value);
  }

  draggedItem.value = null;
  draggedList.value = null;
}

// Move selected items from available to selected for recursos
const moveSelectedToSelectedRecurso = () => {
  selectedAvailableRecursoIds.value.forEach(recursoId => {
    if (!selectedRecursoIds.value.includes(recursoId)) {
      selectedRecursoIds.value.push(recursoId);
    }
  });
  selectedAvailableRecursoIds.value = [];
  updateAvailableRecursos();
  filterSelectedRecursos();
}

// Move selected items from selected to available for recursos
const moveSelectedToAvailableRecurso = () => {
  selectedSelectedRecursoIds.value.forEach(recursoId => {
    const index = selectedRecursoIds.value.indexOf(recursoId);
    if (index !== -1) {
      selectedRecursoIds.value.splice(index, 1);
    }
  });
  selectedSelectedRecursoIds.value = [];
  updateAvailableRecursos();
  filterSelectedRecursos();
}

// Handle drag start event for recursos
const dragStartRecurso = (event, id, listType) => {
  draggedRecursoItem.value = id;
  draggedRecursoList.value = listType;
  event.dataTransfer.effectAllowed = 'move';
}

// Handle drop event for recursos
const dropRecurso = (event, targetList) => {
  event.preventDefault();

  if (!draggedRecursoItem.value || draggedRecursoList.value === targetList) return;

  if (draggedRecursoList.value === 'availableRecurso' && targetList === 'selectedRecurso') {
    moveToSelectedRecurso(draggedRecursoItem.value);
  } else if (draggedRecursoList.value === 'selectedRecurso' && targetList === 'availableRecurso') {
    moveToAvailableRecurso(draggedRecursoItem.value);
  }

  draggedRecursoItem.value = null;
  draggedRecursoList.value = null;
}

const reset = () => {
  isEditing.value = false
  id.value = null;
  numero.value = "";

  // Reset to the terapias tab
  activeTab.value = "terapias";

  // Reset terapias
  selectedTerapiaIds.value = [];
  availableTerapiaSearch.value = "";
  selectedTerapiaSearch.value = "";
  selectedAvailableIds.value = [];
  selectedSelectedIds.value = [];
  updateAvailableTerapias();
  filterSelectedTerapias();

  // Reset recursos
  selectedRecursoIds.value = [];
  availableRecursoSearch.value = "";
  selectedRecursoSearch.value = "";
  selectedAvailableRecursoIds.value = [];
  selectedSelectedRecursoIds.value = [];
  updateAvailableRecursos();
  filterSelectedRecursos();
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
