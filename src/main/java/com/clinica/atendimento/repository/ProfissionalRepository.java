package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.Profissional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfissionalRepository extends JpaRepository<Profissional, Long> {

    @Query("""
            select p
            from Profissional p
                inner join fetch p.pessoa
                left join fetch p.terapias t
                left join fetch t.terapia
            order by p.pessoa.nome""")
    List<Profissional> findAllProfissionais();

    @Query("""
            select p
            from Profissional p
                inner join fetch p.pessoa
                left join fetch p.terapias t
                left join fetch t.terapia
            where p.id = :id""")
    Profissional findProfissionalById(@Param("id") Long id);
}