package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Convenio;
import com.clinica.atendimento.model.ConvenioPaciente;
import com.clinica.atendimento.model.Paciente;
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
public class ConvenioPacienteDTO {

    private Long id;
    private ConvenioDTO convenio;
    private Long pacienteId;
    private String numero;

    // Static method to convert from entity to DTO
    public static ConvenioPacienteDTO fromEntity(ConvenioPaciente convenioPaciente) {
        if (convenioPaciente == null) {
            return null;
        }

        return ConvenioPacienteDTO.builder()
                .id(convenioPaciente.id())
                .convenio(ConvenioDTO.fromEntity(convenioPaciente.convenio()))
                .pacienteId(convenioPaciente.paciente().pessoa().id())
                .numero(convenioPaciente.numero())
                .build();
    }

    // Method to convert from DTO to entity
    public ConvenioPaciente toEntity(Paciente paciente) {
        Convenio convenioEntity = convenio != null ? convenio.toEntity() : null;
        return ConvenioPaciente.builder()
                .id(id)
                .convenio(convenioEntity)
                .paciente(paciente)
                .numero(numero)
                .build();
    }

    // Overloaded method for use in PacienteDTO
    public ConvenioPaciente toEntity() {
        throw new UnsupportedOperationException("Cannot convert ConvenioPacienteDTO to entity without a Paciente instance");
    }
}
