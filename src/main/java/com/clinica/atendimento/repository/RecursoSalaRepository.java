package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.RecursoSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoSalaRepository extends JpaRepository<RecursoSala, Long> {
}