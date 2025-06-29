package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Sessao
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessaoRepository : JpaRepository<Sessao, Long>