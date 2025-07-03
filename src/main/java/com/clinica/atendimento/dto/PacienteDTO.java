package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Pessoa;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class PacienteDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private PessoaDTO pessoa;
    @JsonProperty
    private Set<ConvenioPacienteDTO> convenios;
    @JsonProperty
    private Set<ResponsavelPacienteDTO> responsaveis;

    // Static method to convert from entity to DTO
    public static PacienteDTO fromEntity(Paciente paciente) {
        if (paciente == null) {
            return null;
        }

        return new PacienteDTO(
                paciente.pessoa().id(),
                PessoaDTO.fromEntity(paciente.pessoa()),
                paciente.convenios().stream()
                        .map(ConvenioPacienteDTO::fromEntity)
                        .collect(Collectors.toSet()),
                paciente.responsaveis().stream()
                        .map(ResponsavelPacienteDTO::fromEntity)
                        .collect(Collectors.toSet())
        );
    }

    // Method to convert from DTO to entity
    public Paciente toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id);
        }

        // We need to create the Paciente first, then create the relationships
        // This is because of the circular dependency between Paciente and its relationships
        return Paciente.builder()
                .pessoa(pessoaEntity)
                .build();
    }
}
