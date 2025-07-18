package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.Cronograma;
import br.com.estimular.atendimento.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CronogramaRepository extends JpaRepository<Cronograma, Long> {

    @Query("select c from Cronograma c where c.sala = :sala")
    List<Cronograma> findBySala(@Param("sala") Sala sala);

}