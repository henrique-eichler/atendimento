package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoSalaRepository extends JpaRepository<RecursoSala, Long> {
    @Modifying
    @Query("DELETE FROM RecursoSala rs WHERE rs.sala = ?1")
    void deleteBySala(Sala sala);
}
