package com.clinica.atendimento.repository

import com.clinica.atendimento.model.Sala
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface SalaRepository : JpaRepository<Sala, Long> {

    @Query("""
            select s
            from Sala s
                left join fetch s.terapias ts
                left join fetch ts.terapia
                left join fetch s.cronogramas cs
                left join fetch s.recursos rs
                left join fetch rs.recurso""")
    fun findAllSalas(): List<Sala>

    @Query("""
            select s
            from Sala s
                left join fetch s.terapias ts
                left join fetch ts.terapia
                left join fetch s.cronogramas cs
                left join fetch s.recursos rs
                left join fetch rs.recurso
            where s.id = :id""")
    fun findSalaById(@Param("id") id: Long): Sala
}