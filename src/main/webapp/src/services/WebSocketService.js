import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { ref, readonly } from 'vue'

// Shared connection status that will be accessible to all components
const connected = ref(false)
const pingInterval = 30000 // 30 seconds

// Create a single STOMP client instance
let stompClient = null
let pingTimer = null

// Initialize the WebSocket connection
function initWebSocket() {
  if (stompClient) {
    try {
      stompClient.deactivate()
    } catch (e) {
      console.error('Error deactivating STOMP client', e)
    }
  }

  stompClient = new Client({
    webSocketFactory: () => new SockJS('/ws-cadastro'),
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
  })

  stompClient.onConnect = () => {
    console.log('WebSocket connection established')
    connected.value = true

    // Start ping interval to keep connection alive
    clearInterval(pingTimer)
    pingTimer = setInterval(() => {
      if (stompClient.connected) {
        try {
          stompClient.publish({
            destination: '/app/transcricao/ping'
          })
        } catch (e) {
          console.error('Error sending ping', e)
        }
      }
    }, pingInterval)
  }

  stompClient.onDisconnect = () => {
    console.log('WebSocket connection closed')
    connected.value = false
    clearInterval(pingTimer)
  }

  stompClient.onStompError = frame => {
    console.error('STOMP error', frame)
  }

  stompClient.activate()
}

// Subscribe to a topic
function subscribe(destination, callback) {
  if (!stompClient || !stompClient.connected) {
    console.warn('Cannot subscribe: STOMP client is not connected')
    return null
  }
  
  return stompClient.subscribe(destination, callback)
}

// Publish a message
function publish(destination, body = null) {
  if (!stompClient || !stompClient.connected) {
    console.warn('Cannot publish: STOMP client is not connected')
    return false
  }

  try {
    const message = {
      destination: destination
    }
    
    if (body) {
      message.body = typeof body === 'string' ? body : JSON.stringify(body)
    }
    
    stompClient.publish(message)
    return true
  } catch (err) {
    console.error('Error publishing message', err)
    return false
  }
}

// Clean up resources
function disconnect() {
  clearInterval(pingTimer)
  
  if (stompClient) {
    try {
      stompClient.deactivate()
    } catch (err) {
      console.error('Error deactivating STOMP client', err)
    }
    stompClient = null
  }
}

// Get the client instance (for advanced usage)
function getClient() {
  return stompClient
}

// Export the WebSocket service
export default {
  initWebSocket,
  subscribe,
  publish,
  disconnect,
  getClient,
  connected: readonly(connected) // Expose as readonly to prevent external modification
}