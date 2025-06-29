package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Responsavel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ResponsavelRepository : JpaRepository<Responsavel, Long>