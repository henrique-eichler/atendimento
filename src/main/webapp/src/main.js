import {createApp} from 'vue'
import router from './router'
import './style.css'
import './components.css'
import WebSocketService from './services/WebSocketService'
import ConnectionStatus from './components/ConnectionStatus.vue'

// Initialize the WebSocket service
WebSocketService.initWebSocket()

const app = createApp({
    components: {
        ConnectionStatus
    },
    template: `
      <div>
        <div class="app-header">
          <h1>📋 Sistema de Atendimento Clínico</h1>
          <ConnectionStatus/>
        </div>
        <nav class="main-nav">
          <router-link to="/terapias">Terapias</router-link>
          |
          <router-link to="/salas">Salas</router-link>
          |
          <router-link to="/recursos">Recursos</router-link>
          |
          <router-link to="/transcricao">Transcrição</router-link>
          |
          <router-link to="/connections">Clientes Conectados</router-link>
          |
        </nav>
        <div class="content">
          <router-view></router-view>
        </div>
      </div>
    `
})

app.use(router)
app.mount('#app')
