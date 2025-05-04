import {createRouter, createWebHistory} from 'vue-router'
import CadastroPaciente from './components/CadastroPaciente.vue'
import CadastroResponsavel from './components/CadastroResponsavel.vue'
import CadastroTerapia from './components/CadastroTerapia.vue'
import CadastroProfissional from './components/CadastroProfissional.vue'
import CadastroAgenda from './components/CadastroAgenda.vue'
import TranscricaoAudio from './components/TranscricaoAudio.vue'

const routes = [
    {
        path: '/',
        redirect: '/pacientes'
    },
    {
        path: '/pacientes',
        name: 'Pacientes',
        component: CadastroPaciente
    },
    {
        path: '/responsaveis',
        name: 'Responsáveis',
        component: CadastroResponsavel
    },
    {
        path: '/terapias',
        name: 'Terapias',
        component: CadastroTerapia
    },
    {
        path: '/profissionais',
        name: 'Profissionais',
        component: CadastroProfissional
    },
    {
        path: '/agenda',
        name: 'Agenda',
        component: CadastroAgenda
    },
    {
        path: '/transcricao',
        name: 'Transcrição',
        component: TranscricaoAudio
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router