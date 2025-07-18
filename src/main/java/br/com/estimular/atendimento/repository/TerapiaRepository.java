package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.Terapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerapiaRepository extends JpaRepository<Terapia, Long> {
}