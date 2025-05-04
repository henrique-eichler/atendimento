package com.clinica.atendimento.repository;

import com.clinica.atendimento.model.GrupoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrupoUsuarioRepository extends JpaRepository<GrupoUsuario, Long> {
}