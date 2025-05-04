package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.GestaoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GestaoDocumentoRepository extends JpaRepository<GestaoDocumento, Long> {
}