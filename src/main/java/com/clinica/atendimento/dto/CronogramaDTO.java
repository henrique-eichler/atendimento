package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Cronograma;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.enums.DiaSemana;

import java.time.Instant;

public record CronogramaDTO(
        Long id,
        SalaDTO sala,
        DiaSemana diaSemana,
        Instant horaInicio,
        Instant horaTermino
) {
    // Static method to convert from entity to DTO
    public static CronogramaDTO fromEntity(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return new CronogramaDTO(
            cronograma.id(),
            SalaDTO.fromEntity(cronograma.sala()),
            cronograma.diaSemana(),
            cronograma.horaInicio(),
            cronograma.horaTermino()
        );
    }

    // Method to convert from DTO to entity
    public Cronograma toEntity() {
        Sala salaEntity = sala != null ? sala.toEntity() : null;

        return Cronograma.builder()
            .id(id)
            .sala(salaEntity)
            .diaSemana(diaSemana)
            .horaInicio(horaInicio)
            .horaTermino(horaTermino)
            .build();
    }
}
