import { Client } from '@stomp/stompjs'
import SockJS from 'sockjs-client'

let client
let conected = false

export function useWebSocket() {
  if (!client) {
    client = new Client({
      webSocketFactory: () => new SockJS('http://192.168.122.150:8080/ws-cadastro'),
      reconnectDelay: 5000,
      onConnect: () => conected = true,
      onStompError: (frame) => {
        console.error('Cadastro - Erro STOMP:', frame.headers['message'], frame.body)
      },
      onWebSocketErro: (error) => {
        console.error('Cadastro - WebSocket error: ', error);
      },
      onWebSocketClose: (event) => {
        console.warn('Cadastro - WebSocket close: ', event.reason);
      },
      debug: (str) => {
        console.log('Cadastro - debug: ', str);
      },
      onDisconect: (frame) => conected = false
    })

    client.activate()
  }

  const sendMessage = (destination, content) => {
    if (client.connected) {
      let body = typeof content === 'string' ? content : JSON.stringify(body);
      client.publish({destination, body})
    } else {
      console.warn('Cadastro - WebSocket ainda não conectado. Tentando enviar novamente em 1s...')
      setTimeout(() => sendMessage(destination, body), 1000)
    }
  }

  const isConnected = () => conected

  return { sendMessage, isConnected }
}