package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Cronograma
import com.clinica.atendimento.model.Sala
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface CronogramaRepository : JpaRepository<Cronograma, Long> {

    @Query("select c from Cronograma c where c.sala = :sala")
    fun findBySala(@Param("sala") sala: Sala): List<Cronograma>
}