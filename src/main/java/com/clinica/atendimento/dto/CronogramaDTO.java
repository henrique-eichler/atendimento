package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Cronograma;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.enums.DiaSemana;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaDTO {
    private Long id;
    private SalaDTO sala;
    private DiaSemana diaSemana;
    private Instant horaInicio;
    private Instant horaTermino;
    // Static method to convert from entity to DTO
    public static CronogramaDTO fromEntity(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return CronogramaDTO.builder()
            .id(cronograma.id())
            .sala(SalaDTO.fromEntity(cronograma.sala()))
            .diaSemana(cronograma.diaSemana())
            .horaInicio(cronograma.horaInicio())
            .horaTermino(cronograma.horaTermino())
            .build();
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
