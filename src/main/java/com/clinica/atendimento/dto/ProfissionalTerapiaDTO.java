package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Profissional;
import com.clinica.atendimento.model.ProfissionalTerapia;
import com.clinica.atendimento.model.Terapia;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfissionalTerapiaDTO {
    private Long id;
    private ProfissionalDTO profissional;
    private TerapiaDTO terapia;
    private LocalDate dataValidade;

    // Static method to convert from entity to DTO
    public static ProfissionalTerapiaDTO fromEntity(ProfissionalTerapia profissionalTerapia) {
        if (profissionalTerapia == null) {
            return null;
        }

        return ProfissionalTerapiaDTO.builder()
                .id(profissionalTerapia.id())
                .profissional(ProfissionalDTO.fromEntity(profissionalTerapia.profissional()))
                .terapia(TerapiaDTO.fromEntity(profissionalTerapia.terapia()))
                .dataValidade(profissionalTerapia.dataValidade())
                .build();
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
