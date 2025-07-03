package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Agenda;
import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.Sessao;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class SessaoDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private AgendaDTO agenda;
    @JsonProperty
    private Instant dataInicio;
    @JsonProperty
    private Instant dataTermino;
    @JsonProperty
    private SalaDTO sala;
    @JsonProperty
    private PacienteDTO paciente;

    // Static method to convert from entity to DTO
    public static SessaoDTO fromEntity(Sessao sessao) {
        if (sessao == null) {
            return null;
        }

        return SessaoDTO.builder()
                .id(sessao.id())
                .agenda(AgendaDTO.fromEntity(sessao.agenda()))
                .dataInicio(sessao.dataInicio())
                .dataTermino(sessao.dataTermino())
                .sala(SalaDTO.fromEntity(sessao.sala()))
                .paciente(PacienteDTO.fromEntity(sessao.paciente()))
                .build();
    }

    // Method to convert from DTO to entity
    public Sessao toEntity() {
        Agenda agendaEntity = agenda != null ? agenda.toEntity() : null;
        Sala salaEntity = sala != null ? sala.toEntity() : null;
        Paciente pacienteEntity = paciente != null ? paciente.toEntity() : null;

        return Sessao.builder()
                .id(id)
                .agenda(agendaEntity)
                .dataInicio(dataInicio)
                .dataTermino(dataTermino)
                .sala(salaEntity)
                .paciente(pacienteEntity)
                .build();
    }
}
