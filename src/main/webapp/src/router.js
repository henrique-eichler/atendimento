import {createRouter, createWebHistory} from 'vue-router'
import CadastroTerapia from './components/CadastroTerapia.vue'
import TranscricaoAudio from './components/TranscricaoAudio.vue'
import CadastroSala from './components/CadastroSala.vue'
import CadastroRecurso from './components/CadastroRecurso.vue'
import ConnectedClients from './components/ConnectedClients.vue'

const routes = [
    {
        path: '/terapias',
        name: 'Terapias',
        component: CadastroTerapia
    },
    {
        path: '/transcricao',
        name: 'Transcrição',
        component: TranscricaoAudio
    },
    {
        path: '/salas',
        name: 'Salas',
        component: CadastroSala
    },
    {
        path: '/recursos',
        name: 'Recursos',
        component: CadastroRecurso
    },
    {
        path: '/connections',
        name: 'Clientes Conectados',
        component: ConnectedClients
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
