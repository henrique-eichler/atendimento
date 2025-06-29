package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Recurso
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RecursoRepository : JpaRepository<Recurso, Long>