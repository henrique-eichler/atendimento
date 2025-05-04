package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Gestao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestaoRepository extends JpaRepository<Gestao, Long> {
}