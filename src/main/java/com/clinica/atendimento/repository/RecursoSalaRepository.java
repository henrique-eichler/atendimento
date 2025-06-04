package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.TerapiaSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecursoSalaRepository extends JpaRepository<RecursoSala, Long> {

    List<RecursoSala> findBySala(Sala sala);

    @Modifying
    @Query("DELETE FROM RecursoSala rs WHERE rs.sala = ?1")
    void deleteBySala(Sala sala);
}
