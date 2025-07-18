package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class AgendaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private LocalDate dataAgenda;
    @JsonProperty
    private CronogramaDTO cronograma;
    @JsonProperty
    private PacienteDTO paciente;
    @JsonProperty
    private TerapiaDTO terapia;
    @JsonProperty
    private ConvenioDTO convenio;
    @JsonProperty
    private ProfissionalDTO profissional;

    // Static method to convert from entity to DTO
    public static AgendaDTO fromEntity(Agenda agenda) {
        if (agenda == null) {
            return null;
        }

        return AgendaDTO.builder()
                .id(agenda.id())
                .dataAgenda(agenda.dataAgenda())
                .cronograma(CronogramaDTO.fromEntity(agenda.cronograma()))
                .paciente(PacienteDTO.fromEntity(agenda.paciente()))
                .terapia(TerapiaDTO.fromEntity(agenda.terapia()))
                .convenio(ConvenioDTO.fromEntity(agenda.convenio()))
                .profissional(ProfissionalDTO.fromEntity(agenda.profissional()))
                .build();
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
