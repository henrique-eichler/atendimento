package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Grupo;
import com.clinica.atendimento.model.GrupoRecurso;
import com.clinica.atendimento.model.Recurso;
import com.clinica.atendimento.model.enums.TipoAcesso;

public record GrupoRecursoDTO(
        Long id,
        GrupoDTO grupo,
        RecursoDTO recurso,
        TipoAcesso tipo
) {
    // Static method to convert from entity to DTO
    public static GrupoRecursoDTO fromEntity(GrupoRecurso grupoRecurso) {
        if (grupoRecurso == null) {
            return null;
        }
        
        return new GrupoRecursoDTO(
            grupoRecurso.id(),
            GrupoDTO.fromEntity(grupoRecurso.grupo()),
            RecursoDTO.fromEntity(grupoRecurso.recurso()),
            grupoRecurso.tipo()
        );
    }

    // Method to convert from DTO to entity
    public GrupoRecurso toEntity() {
        Grupo grupoEntity = grupo != null ? grupo.toEntity() : null;
        Recurso recursoEntity = recurso != null ? recurso.toEntity() : null;
        
        return new GrupoRecurso(
            id,
            grupoEntity,
            recursoEntity,
            tipo
        );
    }
}