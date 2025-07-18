package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.Sala;
import br.com.estimular.atendimento.model.TerapiaSala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TerapiaSalaRepository extends JpaRepository<TerapiaSala, Long> {

    @Query("""
            select ts
            from TerapiaSala ts
                left join fetch ts.terapia
            where ts.sala = :sala""")
    List<TerapiaSala> findBySala(@Param("sala") Sala sala);
}
