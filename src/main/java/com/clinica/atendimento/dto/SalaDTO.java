package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Sala;

public record SalaDTO(
    Long id,
    Long numero
) {
    // Static method to convert from entity to DTO
    public static SalaDTO fromEntity(Sala sala) {
        if (sala == null) {
            return null;
        }

        return new SalaDTO(
            sala.id(),
            sala.numero()
        );
    }

    // Method to convert from DTO to entity
    public Sala toEntity() {
        Sala.SalaBuilder builder = Sala.builder();

        // Only set ID if it's not null
        if (id != null) {
            builder.id(id);
        }

        return builder
            .numero(numero)
            .build();
    }
}
