package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Long> {

    @Query("""
            select s
            from Sala s
                left join fetch s.terapias ts
                left join fetch ts.terapia
                left join fetch s.cronogramas cs
                left join fetch s.recursos rs
                left join fetch rs.recurso""")
    List<Sala> findAllSalas();

    @Query("""
            select s
            from Sala s
                left join fetch s.terapias ts
                left join fetch ts.terapia
                left join fetch s.cronogramas cs
                left join fetch s.recursos rs
                left join fetch rs.recurso
            where s.id = :id""")
    Sala findSalaById(@Param("id") Long id);
}