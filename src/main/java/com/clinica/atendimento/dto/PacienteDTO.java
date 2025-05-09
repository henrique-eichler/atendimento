package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Pessoa;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode
@ToString
public class PacienteDTO {
    private final Long id;
    private final PessoaDTO pessoa;
    private final Set<ConvenioPacienteDTO> convenios;
    private final Set<ResponsavelPacienteDTO> responsaveis;

    // Constructor with default values for collections
    public PacienteDTO(Long id, PessoaDTO pessoa, Set<ConvenioPacienteDTO> convenios, Set<ResponsavelPacienteDTO> responsaveis) {
        this.id = id;
        this.pessoa = pessoa;
        this.convenios = convenios != null ? convenios : Collections.emptySet();
        this.responsaveis = responsaveis != null ? responsaveis : Collections.emptySet();
    }

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
