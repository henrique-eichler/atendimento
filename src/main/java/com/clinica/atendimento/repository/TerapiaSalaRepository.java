package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.TerapiaSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerapiaSalaRepository extends JpaRepository<TerapiaSala, Long> {
}