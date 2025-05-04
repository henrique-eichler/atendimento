package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Terapia;

public record TerapiaDTO(
    Long id,
    String nome
) {
    // Static method to convert from entity to DTO
    public static TerapiaDTO fromEntity(Terapia terapia) {
        if (terapia == null) {
            return null;
        }
        
        return new TerapiaDTO(
            terapia.id(),
            terapia.nome()
        );
    }

    // Method to convert from DTO to entity
    public Terapia toEntity() {
        return new Terapia(
            id,
            nome
        );
    }
}