package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.TerapiaConvenio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TerapiaConvenioRepository extends JpaRepository<TerapiaConvenio, Long> {
}