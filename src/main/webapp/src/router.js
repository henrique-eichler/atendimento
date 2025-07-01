import {createRouter, createWebHistory} from 'vue-router'
import TerapiaCadastro from './components/TerapiaCadastro.vue'
import TranscricaoAudio from './components/TranscricaoAudio.vue'
import SalaCadastro from './components/SalaCadastro.vue'
import RecursoCadastro from './components/RecursoCadastro.vue'
import ConnectedClients from './components/ConnectedClients.vue'
import ProfissionalCadastro from "./components/ProfissionalCadastro.vue";

const routes = [
    {
        path: '/terapias',
        name: 'Terapias',
        component: TerapiaCadastro
    },
    {
        path: '/profissionais',
        name: 'Profissionais',
        component: ProfissionalCadastro
    },
    {
        path: '/transcricao',
        name: 'Transcrição',
        component: TranscricaoAudio
    },
    {
        path: '/salas',
        name: 'Salas',
        component: SalaCadastro
    },
    {
        path: '/recursos',
        name: 'Recursos',
        component: RecursoCadastro
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
