package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.*;

import java.time.LocalDate;

public record AgendaDTO(
    Long id,
    LocalDate dataAgenda,
    CronogramaDTO cronograma,
    PacienteDTO paciente,
    TerapiaDTO terapia,
    ConvenioDTO convenio,
    ProfissionalDTO profissional
) {
    // Static method to convert from entity to DTO
    public static AgendaDTO fromEntity(Agenda agenda) {
        if (agenda == null) {
            return null;
        }

        return new AgendaDTO(
            agenda.id(),
            agenda.dataAgenda(),
            CronogramaDTO.fromEntity(agenda.cronograma()),
            PacienteDTO.fromEntity(agenda.paciente()),
            TerapiaDTO.fromEntity(agenda.terapia()),
            ConvenioDTO.fromEntity(agenda.convenio()),
            ProfissionalDTO.fromEntity(agenda.profissional())
        );
    }

    // Method to convert from DTO to entity
    public Agenda toEntity() {
        Cronograma cronogramaEntity = cronograma != null ? cronograma.toEntity() : null;
        Paciente pacienteEntity = paciente != null ? paciente.toEntity() : null;
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;
        Convenio convenioEntity = convenio != null ? convenio.toEntity() : null;
        Profissional profissionalEntity = profissional != null ? profissional.toEntity() : null;

        return Agenda.builder()
            .id(id)
            .dataAgenda(dataAgenda)
            .cronograma(cronogramaEntity)
            .paciente(pacienteEntity)
            .terapia(terapiaEntity)
            .convenio(convenioEntity)
            .profissional(profissionalEntity)
            .build();
    }
}
