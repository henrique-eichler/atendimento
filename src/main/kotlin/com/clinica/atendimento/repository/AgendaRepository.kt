package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Agenda
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface AgendaRepository : JpaRepository<Agenda, Long>