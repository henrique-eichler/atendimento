package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Grupo;

public record GrupoDTO(
    Long id,
    String nome,
    Long grupoPaiId
) {
    // Static method to convert from entity to DTO
    public static GrupoDTO fromEntity(Grupo grupo) {
        if (grupo == null) {
            return null;
        }
        
        return new GrupoDTO(
            grupo.id(),
            grupo.nome(),
            grupo.grupoPai() != null ? grupo.grupoPai().id() : null
        );
    }

    // Method to convert from DTO to entity
    public Grupo toEntity(Grupo grupoPaiEntity) {
        return new Grupo(
            id,
            nome,
            grupoPaiEntity
        );
    }
    
    // Overloaded method for use when grupoPai is not available
    public Grupo toEntity() {
        return new Grupo(
            id,
            nome,
            null
        );
    }
}