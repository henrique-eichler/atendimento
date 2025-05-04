package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {
}