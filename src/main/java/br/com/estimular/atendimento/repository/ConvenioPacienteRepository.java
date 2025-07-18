package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.ConvenioPaciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConvenioPacienteRepository extends JpaRepository<ConvenioPaciente, Long> {
}