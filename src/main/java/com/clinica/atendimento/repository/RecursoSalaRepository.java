package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoSalaRepository extends JpaRepository<RecursoSala, Long> {

    @Query("select rs from RecursoSala rs left join fetch rs.recurso where rs.sala = :sala")
    List<RecursoSala> findBySala(@Param("sala") Sala sala);

    @Modifying
    @Query("DELETE FROM RecursoSala rs WHERE rs.sala = ?1")
    void deleteBySala(Sala sala);
}
