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
        // Using the constructor for backward compatibility
        // return new Terapia(id, nome);

        // Using the builder pattern
        Terapia.TerapiaBuilder builder = Terapia.builder();

        // Only set ID if it's not null
        if (id != null) {
            builder.id(id);
        }

        return builder
            .nome(nome)
            .build();
    }
}
