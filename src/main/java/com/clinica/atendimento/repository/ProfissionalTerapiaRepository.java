package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.ProfissionalTerapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfissionalTerapiaRepository extends JpaRepository<ProfissionalTerapia, Long> {
}