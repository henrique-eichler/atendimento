package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.ConvenioPaciente;
import com.clinica.atendimento.model.Convenio;
import com.clinica.atendimento.model.Paciente;

public record ConvenioPacienteDTO(
    Long id,
    ConvenioDTO convenio,
    Long pacienteId,
    String numero
) {
    // Static method to convert from entity to DTO
    public static ConvenioPacienteDTO fromEntity(ConvenioPaciente convenioPaciente) {
        if (convenioPaciente == null) {
            return null;
        }
        
        return new ConvenioPacienteDTO(
            convenioPaciente.id(),
            ConvenioDTO.fromEntity(convenioPaciente.convenio()),
            convenioPaciente.paciente().id(),
            convenioPaciente.numero()
        );
    }

    // Method to convert from DTO to entity
    public ConvenioPaciente toEntity(Paciente paciente) {
        Convenio convenioEntity = convenio != null ? convenio.toEntity() : null;
        return new ConvenioPaciente(
            id,
            convenioEntity,
            paciente,
            numero
        );
    }
    
    // Overloaded method for use in PacienteDTO
    public ConvenioPaciente toEntity() {
        throw new UnsupportedOperationException("Cannot convert ConvenioPacienteDTO to entity without a Paciente instance");
    }
}