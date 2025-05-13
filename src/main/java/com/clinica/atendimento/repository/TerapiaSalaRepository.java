package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.TerapiaSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapiaSalaRepository extends JpaRepository<TerapiaSala, Long> {
    List<TerapiaSala> findBySala(Sala sala);

    void deleteBySala(Sala sala);
}
