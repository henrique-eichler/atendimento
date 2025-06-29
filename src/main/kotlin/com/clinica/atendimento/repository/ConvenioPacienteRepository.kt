package com.clinica.atendimento.repository

import com.clinica.atendimento.model.ConvenioPaciente
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConvenioPacienteRepository : JpaRepository<ConvenioPaciente, Long>