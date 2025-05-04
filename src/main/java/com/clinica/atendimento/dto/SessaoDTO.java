package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Agenda;
import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.Sessao;

import java.time.Instant;

public record SessaoDTO(
    Long id,
    AgendaDTO agenda,
    Instant dataInicio,
    Instant dataTermino,
    SalaDTO sala,
    PacienteDTO paciente
) {
    // Static method to convert from entity to DTO
    public static SessaoDTO fromEntity(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        
        return new SessaoDTO(
            sessao.id(),
            AgendaDTO.fromEntity(sessao.agenda()),
            sessao.dataInicio(),
            sessao.dataTermino(),
            SalaDTO.fromEntity(sessao.sala()),
            PacienteDTO.fromEntity(sessao.paciente())
        );
    }

    // Method to convert from DTO to entity
    public Sessao toEntity() {
        Agenda agendaEntity = agenda != null ? agenda.toEntity() : null;
        Sala salaEntity = sala != null ? sala.toEntity() : null;
        Paciente pacienteEntity = paciente != null ? paciente.toEntity() : null;
        
        return new Sessao(
            id,
            agendaEntity,
            dataInicio,
            dataTermino,
            salaEntity,
            pacienteEntity
        );
    }
}