package br.com.estimular.atendimento.repository;

import br.com.estimular.atendimento.model.Profissional;
import br.com.estimular.atendimento.model.ProfissionalTerapia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalTerapiaRepository extends JpaRepository<ProfissionalTerapia, Long> {

    @Query("""
            select pt
            from ProfissionalTerapia pt
                left join fetch pt.terapia
            where pt.profissional = :profissional""")
    List<ProfissionalTerapia> findByProfissional(@Param("profissional") Profissional profissional);
}