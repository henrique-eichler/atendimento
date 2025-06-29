package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Profissional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfissionalRepository : JpaRepository<Profissional, Long>