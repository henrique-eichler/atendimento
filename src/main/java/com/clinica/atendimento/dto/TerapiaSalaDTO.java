package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.model.TerapiaSala;

public record TerapiaSalaDTO(
    Long id,
    TerapiaDTO terapia,
    SalaDTO sala
) {
    // Static method to convert from entity to DTO
    public static TerapiaSalaDTO fromEntity(TerapiaSala terapiaSala) {
        if (terapiaSala == null) {
            return null;
        }

        return new TerapiaSalaDTO(
            terapiaSala.id(),
            TerapiaDTO.fromEntity(terapiaSala.terapia()),
            SalaDTO.fromEntity(terapiaSala.sala())
        );
    }

    // Method to convert from DTO to entity
    public TerapiaSala toEntity() {
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;
        Sala salaEntity = sala != null ? sala.toEntity() : null;

        return TerapiaSala.builder()
            .id(id)
            .terapia(terapiaEntity)
            .sala(salaEntity)
            .build();
    }
}