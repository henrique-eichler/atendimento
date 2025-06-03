<template>
  <div class="connected-clients">
    <h1>Clientes Conectados</h1>
    
    <div class="refresh-controls">
      <button @click="fetchClients" class="refresh-button">Atualizar</button>
      <label>
        <input type="checkbox" v-model="autoRefresh"> Auto-atualizar (10s)
      </label>
    </div>
    
    <div v-if="loading" class="loading">
      Carregando...
    </div>
    
    <div v-else-if="error" class="error">
      Erro ao carregar clientes: {{ error }}
    </div>
    
    <div v-else-if="clients.length === 0" class="no-clients">
      Nenhum cliente conectado no momento.
    </div>
    
    <table v-else class="clients-table">
      <thead>
        <tr>
          <th>ID do Cliente</th>
          <th>Endereço Remoto</th>
          <th>Status</th>
          <th>Última Atividade</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="client in clients" :key="client.clientId">
          <td>{{ client.clientId }}</td>
          <td>{{ client.remoteAddress }}</td>
          <td>
            <span :class="client.connected ? 'status-connected' : 'status-disconnected'">
              {{ client.connected ? 'Conectado' : 'Desconectado' }}
            </span>
          </td>
          <td>{{ formatDate(client.lastActivity) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
export default {
  name: 'ConnectedClients',
  data() {
    return {
      clients: [],
      loading: true,
      error: null,
      autoRefresh: false,
      refreshInterval: null
    }
  },
  mounted() {
    this.fetchClients()
  },
  watch: {
    autoRefresh(newValue) {
      if (newValue) {
        this.startAutoRefresh()
      } else {
        this.stopAutoRefresh()
      }
    }
  },
  beforeUnmount() {
    this.stopAutoRefresh()
  },
  methods: {
    async fetchClients() {
      this.loading = true
      this.error = null
      
      try {
        const response = await fetch('/api/connections')
        if (!response.ok) {
          throw new Error(`HTTP error! status: ${response.status}`)
        }
        this.clients = await response.json()
      } catch (e) {
        console.error('Error fetching clients:', e)
        this.error = e.message
      } finally {
        this.loading = false
      }
    },
    formatDate(dateString) {
      try {
        const date = new Date(dateString)
        return date.toLocaleString()
      } catch (e) {
        return dateString
      }
    },
    startAutoRefresh() {
      this.refreshInterval = setInterval(() => {
        this.fetchClients()
      }, 10000) // Refresh every 10 seconds
    },
    stopAutoRefresh() {
      if (this.refreshInterval) {
        clearInterval(this.refreshInterval)
        this.refreshInterval = null
      }
    }
  }
}
</script>

<style scoped>
.connected-clients {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
}

h1 {
  margin-bottom: 20px;
}

.refresh-controls {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 15px;
}

.refresh-button {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.refresh-button:hover {
  background-color: #45a049;
}

.loading, .error, .no-clients {
  padding: 20px;
  text-align: center;
  background-color: #f8f9fa;
  border-radius: 4px;
  margin-bottom: 20px;
}

.error {
  color: #721c24;
  background-color: #f8d7da;
}

.clients-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.clients-table th, .clients-table td {
  padding: 12px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.clients-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.clients-table tr:hover {
  background-color: #f5f5f5;
}

.status-connected {
  color: #28a745;
  font-weight: bold;
}

.status-disconnected {
  color: #dc3545;
}
</style>