package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.GrupoRecurso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoRecursoRepository extends JpaRepository<GrupoRecurso, Long> {
}