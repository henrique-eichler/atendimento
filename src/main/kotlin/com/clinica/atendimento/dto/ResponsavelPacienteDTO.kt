package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Paciente
import com.clinica.atendimento.model.Responsavel
import com.clinica.atendimento.model.ResponsavelPaciente
import com.clinica.atendimento.model.enums.GrauParentesco
import com.fasterxml.jackson.annotation.JsonProperty

data class ResponsavelPacienteDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var responsavel: ResponsavelDTO? = null,
    
    @JsonProperty
    var pacienteId: Long? = null,
    
    @JsonProperty
    var grauParentesco: GrauParentesco? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): ResponsavelPacienteDTO {
        this.id = id
        return this
    }
    
    fun responsavel(): ResponsavelDTO? = responsavel
    
    fun responsavel(responsavel: ResponsavelDTO?): ResponsavelPacienteDTO {
        this.responsavel = responsavel
        return this
    }
    
    fun pacienteId(): Long? = pacienteId
    
    fun pacienteId(pacienteId: Long?): ResponsavelPacienteDTO {
        this.pacienteId = pacienteId
        return this
    }
    
    fun grauParentesco(): GrauParentesco? = grauParentesco
    
    fun grauParentesco(grauParentesco: GrauParentesco?): ResponsavelPacienteDTO {
        this.grauParentesco = grauParentesco
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(paciente: Paciente): ResponsavelPaciente {
        val responsavelEntity = responsavel?.toEntity()
        return ResponsavelPaciente.builder()
            .id(id)
            .responsavel(responsavelEntity)
            .paciente(paciente)
            .grauParentesco(grauParentesco)
            .build()
    }

    // Overloaded method for use in PacienteDTO
    fun toEntity(): ResponsavelPaciente {
        throw UnsupportedOperationException("Cannot convert ResponsavelPacienteDTO to entity without a Paciente instance")
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(responsavelPaciente: ResponsavelPaciente?): ResponsavelPacienteDTO? {
            if (responsavelPaciente == null) {
                return null
            }

            return ResponsavelPacienteDTO(
                id = responsavelPaciente.id(),
                responsavel = ResponsavelDTO.fromEntity(responsavelPaciente.responsavel()),
                pacienteId = responsavelPaciente.paciente().pessoa().id(),
                grauParentesco = responsavelPaciente.grauParentesco()
            )
        }

        @JvmStatic
        fun builder(): ResponsavelPacienteDTOBuilder {
            return ResponsavelPacienteDTOBuilder()
        }
    }

    class ResponsavelPacienteDTOBuilder {
        private val instance = ResponsavelPacienteDTO()

        fun id(id: Long?): ResponsavelPacienteDTOBuilder {
            instance.id = id
            return this
        }

        fun responsavel(responsavel: ResponsavelDTO?): ResponsavelPacienteDTOBuilder {
            instance.responsavel = responsavel
            return this
        }

        fun pacienteId(pacienteId: Long?): ResponsavelPacienteDTOBuilder {
            instance.pacienteId = pacienteId
            return this
        }

        fun grauParentesco(grauParentesco: GrauParentesco?): ResponsavelPacienteDTOBuilder {
            instance.grauParentesco = grauParentesco
            return this
        }

        fun build(): ResponsavelPacienteDTO {
            return instance
        }
    }
}