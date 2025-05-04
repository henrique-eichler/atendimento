package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Profissional;
import com.clinica.atendimento.model.ProfissionalTerapia;
import com.clinica.atendimento.model.Terapia;

import java.time.LocalDate;

public record ProfissionalTerapiaDTO(
    Long id,
    ProfissionalDTO profissional,
    TerapiaDTO terapia,
    LocalDate dataValidade
) {
    // Static method to convert from entity to DTO
    public static ProfissionalTerapiaDTO fromEntity(ProfissionalTerapia profissionalTerapia) {
        if (profissionalTerapia == null) {
            return null;
        }

        return new ProfissionalTerapiaDTO(
            profissionalTerapia.id(),
            ProfissionalDTO.fromEntity(profissionalTerapia.profissional()),
            TerapiaDTO.fromEntity(profissionalTerapia.terapia()),
            profissionalTerapia.dataValidade()
        );
    }

    // Method to convert from DTO to entity
    public ProfissionalTerapia toEntity() {
        Profissional profissionalEntity = profissional != null ? profissional.toEntity() : null;
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;

        return ProfissionalTerapia.builder()
            .id(id)
            .profissional(profissionalEntity)
            .terapia(terapiaEntity)
            .dataValidade(dataValidade)
            .build();
    }
}