
package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.responsaveis")
    List<Paciente> findAllWithResponsaveis();

    @Query("SELECT p FROM Paciente p LEFT JOIN FETCH p.responsaveis WHERE p.id = :id")
    Optional<Paciente> findByIdWithResponsaveis(@Param("id") Long id);
}
