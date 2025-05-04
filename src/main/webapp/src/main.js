import { createApp } from 'vue'
import router from './router'
import './style.css'

const app = createApp({
  template: `
    <div>
      <h1>📋 Sistema de Atendimento Clínico</h1>
      <nav class="main-nav">
        <router-link to="/pacientes">Pacientes</router-link> |
        <router-link to="/responsaveis">Responsáveis</router-link> |
        <router-link to="/terapias">Terapias</router-link> |
        <router-link to="/profissionais">Profissionais</router-link> |
        <router-link to="/agenda">Agenda</router-link> |
        <router-link to="/transcricao">Transcrição</router-link> |
      </nav>
      <div class="content">
        <router-view></router-view>
      </div>
    </div>
  `})

app.use(router)
app.mount('#app')
