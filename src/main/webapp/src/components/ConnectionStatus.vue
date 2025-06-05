<template>
  <div :class="{ 'connected': connected, 'disconnected': !connected }" class="status-badge">
    {{ connected ? 'Conectado' : 'Desconectado' }}
  </div>
</template>

<script setup>
import {ref, watchEffect} from 'vue'
import WebSocketService from '../services/WebSocketService'

// Get the connection status from the WebSocket service
const connected = ref(false)

// Watch for changes in the WebSocketService.connected property
watchEffect(() => {
  connected.value = WebSocketService.connected.value
})
</script>

<style scoped>
.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: bold;
  margin-left: 10px;
}

.connected {
  background-color: #4caf50;
  color: white;
}

.disconnected {
  background-color: #f44336;
  color: white;
}
</style>
