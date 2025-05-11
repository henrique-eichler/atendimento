import SockJS from 'sockjs-client'
import { Client } from '@stomp/stompjs'
import { ref, readonly } from 'vue'

// Shared connection status that will be accessible to all components
const connected = ref(false)

// Create a single STOMP client instance
let stompClient = null

// Message queue for storing messages during disconnection
const messageQueue = []

// Generate or retrieve client UUID
function getClientUuid() {
  let clientUuid = localStorage.getItem('client_uuid')
  if (!clientUuid) {
    clientUuid = generateUuid()
    localStorage.setItem('client_uuid', clientUuid)
  }
  return clientUuid
}

// Generate a UUID v4
function generateUuid() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
    const r = Math.random() * 16 | 0
    const v = c === 'x' ? r : (r & 0x3 | 0x8)
    return v.toString(16)
  })
}

// Client UUID
const clientUuid = getClientUuid()

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
    webSocketFactory: () => new SockJS(`/ws-cadastro?clientUuid=${encodeURIComponent(clientUuid)}`),
    maxWebSocketFrameSize: 16 * 1024,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    // Configure reconnect behavior
    reconnectDelay: 1000, // Start with a 1 second delay
    reconnectDelayMax: 30000, // Max delay of 30 seconds
    reconnectBackoffMultiplier: 1.5, // Exponential backoff
    maxRetries: 10, // Maximum number of reconnect attempts
    connectHeaders: {
      clientUuid: clientUuid // Also include client UUID in connection headers as backup
    },
    debug: function(str) {
      console.log('STOMP Debug:', str);
    }
  })

  stompClient.onConnect = (frame) => {
    console.log('WebSocket connection established', frame)
    connected.value = true

    // Process any queued messages
    if (messageQueue.length > 0) {
      console.log(`Connection restored. Processing ${messageQueue.length} queued messages.`)
      processMessageQueue()
    }

    // Reset reconnect counter on successful connection
    reconnectCount = 0
  }

  stompClient.onDisconnect = (frame) => {
    console.log('WebSocket connection closed', frame)
    connected.value = false
  }

  stompClient.onStompError = frame => {
    console.error('STOMP error', frame)
  }

  // Handle WebSocket errors
  stompClient.onWebSocketError = (event) => {
    console.error('WebSocket error', event)
  }

  // Handle WebSocket close events
  stompClient.onWebSocketClose = (event) => {
    console.log('WebSocket closed', event)
  }

  // Handle reconnect attempts
  let reconnectCount = 0
  stompClient.beforeConnect = () => {
    if (reconnectCount > 0) {
      console.log(`Attempting to reconnect (${reconnectCount})...`)
    }
    reconnectCount++
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

// Process queued messages
function processMessageQueue() {
  if (messageQueue.length > 0 && stompClient && stompClient.connected) {
    console.log(`Processing message queue (${messageQueue.length} messages)`)

    // Create a copy of the queue and clear the original
    const queueCopy = [...messageQueue]
    messageQueue.length = 0

    // Process each message
    queueCopy.forEach(message => {
      try {
        stompClient.publish(message)
        console.log('Queued message sent successfully', message.destination)
      } catch (err) {
        console.error('Error sending queued message', err)
        // If sending fails, add back to queue
        messageQueue.push(message)
      }
    })
  }
}

// Publish a message
function publish(destination, body = null) {
  const message = {
    destination: destination
  }

  if (body) {
    message.body = typeof body === 'string' ? body : JSON.stringify(body)
  }

  // If not connected, queue the message
  if (!stompClient || !stompClient.connected) {
    console.warn('Cannot publish: STOMP client is not connected, message queued')
    messageQueue.push(message)
    return false
  }

  try {
    stompClient.publish(message)
    return true
  } catch (err) {
    console.error('Error publishing message', err)
    // Queue the message if sending fails
    messageQueue.push(message)
    return false
  }
}

// Clean up resources
function disconnect() {
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
  processMessageQueue,
  connected: readonly(connected), // Expose as readonly to prevent external modification
  // Getter for message queue status
  get queueSize() {
    return messageQueue.length;
  },
  // Expose client UUID
  get clientUuid() {
    return clientUuid;
  }
}
