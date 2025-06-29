package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Convenio
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ConvenioRepository : JpaRepository<Convenio, Long>