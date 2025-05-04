package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.ResponsavelPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResponsavelPacienteRepository extends JpaRepository<ResponsavelPaciente, Long> {
}