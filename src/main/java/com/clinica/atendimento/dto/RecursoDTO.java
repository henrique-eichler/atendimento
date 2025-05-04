package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Recurso;
import com.clinica.atendimento.model.enums.TipoRecurso;

public record RecursoDTO(
        Long id,
        String descricao,
        TipoRecurso tipo,
        Long recursoPaiId
) {
    // Static method to convert from entity to DTO
    public static RecursoDTO fromEntity(Recurso recurso) {
        if (recurso == null) {
            return null;
        }
        
        return new RecursoDTO(
            recurso.id(),
            recurso.descricao(),
            recurso.tipo(),
            recurso.recursoPai() != null ? recurso.recursoPai().id() : null
        );
    }

    // Method to convert from DTO to entity
    public Recurso toEntity(Recurso recursoPaiEntity) {
        return new Recurso(
            id,
            descricao,
            tipo,
            recursoPaiEntity
        );
    }
    
    // Overloaded method for use when recursoPai is not available
    public Recurso toEntity() {
        return new Recurso(
            id,
            descricao,
            tipo,
            null
        );
    }
}