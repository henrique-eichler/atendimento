<template>
  <div class="cadastro-container">
    <div class="header">
      <h1>🏢 Cadastro de Profissionais</h1>
    </div>

    <ModalForm :isOpen="isEditing" title="Cadastro de Profissional" @close="cancelar">
      <div class="form-content">
        <div class="form-group">
          <label>Id</label>
          <input v-model="profissional.id" placeholder="Id do Profissional" readonly/>
        </div>
        <div class="form-group">
          <label>Nome</label>
          <input v-model="profissional.pessoa.nome" placeholder="Nome do Profissional" required/>
        </div>

        <!-- Tab Navigation -->
        <div class="tab-navigation">
          <button :class="{ active: activeTab === 'cadastro' }" class="tab-button" @click="activeTab = 'cadastro'">Cadastro</button>
          <button :class="{ active: activeTab === 'terapias' }" class="tab-button" @click="activeTab = 'terapias'">Terapias</button>
          <button :class="{ active: activeTab === 'cronograma' }" class="tab-button" @click="activeTab = 'cronograma'">Cronograma</button>
        </div>

        <!-- Tab Content -->
        <div class="tab-content">
          <!-- Terapias Tab -->
          <div v-if="activeTab === 'cadastro'" class="tab-pane">
            <div class="form-group">
              <label>Email</label>
              <input type="email" v-model="profissional.pessoa.email" placeholder="Email do Profissional" required/>
            </div>
            <div class="form-group">
              <label>Data de nascimento</label>
              <input type="date" v-model="profissional.pessoa.dataNascimento" placeholder="Data de nascimento do Profissional" required/>
            </div>
            <div class="form-group">
              <label>Sexo</label>
              <input v-model="profissional.pessoa.sexo" placeholder="Sexo do Profissional" required/>
            </div>
          </div>
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
                    <div v-for="terapia in terapiasSelecionadas" :key="terapia.id" class="list-item" @click="profissional.terapias = profissional.terapias.filter(t => t.id !== terapia.id)">{{ terapia.nome }}</div>
                  </div>
                </div>

                <div class="list-box">
                  <div class="list-header">
                    <h3>Disponíveis</h3>
                    <input v-model="search.terapias.disponiveis" class="search-input" placeholder="Buscar..."/>
                  </div>
                  <div ref="availableListRef" class="list-content">
                    <div v-for="terapia in terapiasDisponiveis" :key="terapia.id" class="list-item" @click="profissional.terapias.push(terapia)">{{ terapia.nome }}</div>
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
          <h2>📋 Lista de Profissionais</h2>
          <div class="card-buttons">
            <button class="btn btn-primary" @click="listarUsuarios()">
              <span class="icon">🔄</span> Atualizar
            </button>
            <button class="btn btn-primary" @click="novo()">
              <span class="icon">📄</span> Novo Profissional
            </button>
          </div>
        </div>
        <div class="content">
          <table class="data-table">
            <thead>
            <tr>
              <th>Nome</th>
              <th>Terapias</th>
              <th>Ações</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="profissional in profissionais" :key="profissional.id">
              <td>{{ profissional.pessoa.nome }}</td>
              <td>
                <div class="terapias-list">
                  <span v-if="!profissional.terapias || profissional.terapias.length === 0" class="no-terapias">Nenhuma terapia associada</span>
                  <div v-for="terapia in profissional.terapias" v-else :key="terapia.id" class="terapia-tag">{{ terapia.nome }}</div>
                </div>
              </td>
              <td class="actions">
                <button class="btn-icon" @click="editar(profissional)">
                  <span class="icon">📝</span>
                </button>
                <button class="btn-icon" @click="remover(profissional.id)">
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

const profissionais = ref([])
const profissional = ref({id: null, pessoa: {nome: "", email: "", dataNascimento: "", sexo: ""}, terapias: []})
const isEditing = ref(false)

const activeTab = ref("cadastro")
const terapias = ref([])

const search = ref({terapias: {disponiveis: "", selecionadas: ""}})
const terapiasDisponiveis = computed(() => terapias.value.filter(t => t.nome.toLowerCase().includes(search.value.terapias.disponiveis) && !profissional.value.terapias.some(s => s.id === t.id)))
const terapiasSelecionadas = computed(() => terapias.value.filter(t => t.nome.toLowerCase().includes(search.value.terapias.selecionadas) && profissional.value.terapias.some(s => s.id === t.id)))

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(WebSocketService.subscribe("/topic/profissional/response/save", msg => saved(msg.body)))
  subscriptions.push(WebSocketService.subscribe("/topic/profissional/response/delete", msg => deleted(msg.body)))
  subscriptions.push(WebSocketService.subscribe("/topic/profissional/response/list", msg => profissionais.value = msg.body))
  subscriptions.push(WebSocketService.subscribe("/topic/terapia/response/list", msg => terapias.value = msg.body))

  // Send initialization message
  WebSocketService.publish("/topic/profissional/request/list")
  WebSocketService.publish("/topic/terapia/request/list")
})

const listarUsuarios = () => {
  WebSocketService.publish("/topic/profissional/request/list")
}

const saved = profissional => {
  const index = profissionais.value.findIndex(t => t.id === profissional.id);
  if (index !== -1) {
    profissionais.value[index] = profissional; // Replace
  } else {
    profissionais.value.push(profissional);    // Add
  }
}

const deleted = id => {
  profissionais.value = profissionais.value.filter(p => p.id !== id)
}

const novo = () => {
  reset();
  isEditing.value = true;
}

const editar = t => {
  profissional.value.id = t.id;
  profissional.value.pessoa.id = t.pessoa.id;
  profissional.value.pessoa.nome = t.pessoa.nome;
  profissional.value.pessoa.email = t.pessoa.email;
  profissional.value.pessoa.dataNascimento = t.pessoa.dataNascimento;
  profissional.value.pessoa.sexo = t.pessoa.sexo;
  profissional.value.terapias = [...t.terapias];
  isEditing.value = true;
  activeTab.value = "cadastro"
}

const salvar = () => {
  WebSocketService.publish("/topic/profissional/request/save", profissional.value)
  reset();
}

const remover = idToRemove => {
  if (confirm("Excluir?")) {
    WebSocketService.publish("/topic/profissional/request/delete", idToRemove)
  }
}

const cancelar = () => {
  reset()
}

const reset = () => {
  isEditing.value = false
  profissional.value.id = null;
  profissional.value.pessoa.nome = "";
  profissional.value.pessoa.email = "";
  profissional.value.pessoa.dataNascimento = "";
  profissional.value.pessoa.sexo = "";
  profissional.value.terapias = [];
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
