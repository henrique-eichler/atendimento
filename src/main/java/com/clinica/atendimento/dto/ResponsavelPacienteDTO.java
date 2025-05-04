package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Responsavel;
import com.clinica.atendimento.model.ResponsavelPaciente;
import com.clinica.atendimento.model.enums.GrauParentesco;

public record ResponsavelPacienteDTO(
        Long id,
        ResponsavelDTO responsavel,
        Long pacienteId,
        GrauParentesco grauParentesco
) {
    // Static method to convert from entity to DTO
    public static ResponsavelPacienteDTO fromEntity(ResponsavelPaciente responsavelPaciente) {
        if (responsavelPaciente == null) {
            return null;
        }
        
        return new ResponsavelPacienteDTO(
            responsavelPaciente.id(),
            ResponsavelDTO.fromEntity(responsavelPaciente.responsavel()),
            responsavelPaciente.paciente().id(),
            responsavelPaciente.grauParentesco()
        );
    }

    // Method to convert from DTO to entity
    public ResponsavelPaciente toEntity(Paciente paciente) {
        Responsavel responsavelEntity = responsavel != null ? responsavel.toEntity() : null;
        return new ResponsavelPaciente(
            id,
            responsavelEntity,
            paciente,
            grauParentesco
        );
    }
    
    // Overloaded method for use in PacienteDTO
    public ResponsavelPaciente toEntity() {
        throw new UnsupportedOperationException("Cannot convert ResponsavelPacienteDTO to entity without a Paciente instance");
    }
}