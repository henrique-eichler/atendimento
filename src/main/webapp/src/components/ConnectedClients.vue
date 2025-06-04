<template>
  <div class="connected-clients">
    <h1>Clientes Conectados</h1>

    <div class="refresh-controls">
      <button @click="fetchClients" class="refresh-button">Atualizar</button>
      <label>
        <input type="checkbox" @input="togleAutoRefresh"> Auto-atualizar (1s)
      </label>
    </div>

    <table class="clients-table">
      <thead>
      <tr>
        <th>ID do Cliente</th>
        <th>Status</th>
        <th>Conectado desde</th>
        <th>Ultima Mensagem</th>
        <th>Desconectado em</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="session in sessions" :key="session.clientId">
        <td>{{ session.clientId }}</td>
        <td>
            <span :class="session.connected ? 'status-connected' : 'status-disconnected'">
              {{ session.connected ? 'Conectado' : 'Desconectado' }}
            </span>
        </td>
        <td>{{ session.startedAt }}</td>
        <td>{{ session.updatedAt }}</td>
        <td>{{ session.disconectedAt }}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup>
import {onMounted, onUnmounted, ref, watch} from "vue"
import WebSocketService from '../services/WebSocketService'

const sessions = ref([])

const autoRefresh = watch()
const refreshInterval = ref(null)

// Subscriptions
let subscriptions = []

onMounted(() => {
  // Subscribe to topics
  subscriptions.push(
      WebSocketService.subscribe("/topic/sessions/response/list", msg => listed(msg.body))
  )

  // Send initialization message
  WebSocketService.publish("/topic/sessions/request/list")
})

const fetchClients = () => {
  WebSocketService.publish("/topic/sessions/request/list")
}

const listed = list => {
  sessions.value = list
}

const togleAutoRefresh = () => {
  if (refreshInterval.value) {
    clearInterval(refreshInterval.value)
    refreshInterval.value = null
  } else {
    refreshInterval.value = setInterval(fetchClients, 1000)
  }
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