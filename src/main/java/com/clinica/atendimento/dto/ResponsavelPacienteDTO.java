package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Responsavel;
import com.clinica.atendimento.model.ResponsavelPaciente;
import com.clinica.atendimento.model.enums.GrauParentesco;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class ResponsavelPacienteDTO {

    private Long id;
    private ResponsavelDTO responsavel;
    private Long pacienteId;
    private GrauParentesco grauParentesco;

    // Static method to convert from entity to DTO
    public static ResponsavelPacienteDTO fromEntity(ResponsavelPaciente responsavelPaciente) {
        if (responsavelPaciente == null) {
            return null;
        }

        return ResponsavelPacienteDTO.builder()
                .id(responsavelPaciente.id())
                .responsavel(ResponsavelDTO.fromEntity(responsavelPaciente.responsavel()))
                .pacienteId(responsavelPaciente.paciente().pessoa().id())
                .grauParentesco(responsavelPaciente.grauParentesco())
                .build();
    }

    // Method to convert from DTO to entity
    public ResponsavelPaciente toEntity(Paciente paciente) {
        Responsavel responsavelEntity = responsavel != null ? responsavel.toEntity() : null;
        return ResponsavelPaciente.builder()
                .id(id)
                .responsavel(responsavelEntity)
                .paciente(paciente)
                .grauParentesco(grauParentesco)
                .build();
    }

    // Overloaded method for use in PacienteDTO
    public ResponsavelPaciente toEntity() {
        throw new UnsupportedOperationException("Cannot convert ResponsavelPacienteDTO to entity without a Paciente instance");
    }
}
