package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Convenio
import com.clinica.atendimento.model.ConvenioPaciente
import com.clinica.atendimento.model.Paciente
import com.fasterxml.jackson.annotation.JsonProperty

data class ConvenioPacienteDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var convenio: ConvenioDTO? = null,

    @JsonProperty
    var pacienteId: Long? = null,

    @JsonProperty
    var numero: String? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): ConvenioPacienteDTO {
        this.id = id
        return this
    }

    fun convenio(convenio: ConvenioDTO?): ConvenioPacienteDTO {
        this.convenio = convenio
        return this
    }

    fun pacienteId(pacienteId: Long?): ConvenioPacienteDTO {
        this.pacienteId = pacienteId
        return this
    }

    fun numero(numero: String?): ConvenioPacienteDTO {
        this.numero = numero
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(paciente: Paciente): ConvenioPaciente {
        val convenioEntity = convenio?.toEntity()
        return ConvenioPaciente.builder()
            .id(id)
            .convenio(convenioEntity)
            .paciente(paciente)
            .numero(numero)
            .build()
    }

    // Overloaded method for use in PacienteDTO
    fun toEntity(): ConvenioPaciente {
        throw UnsupportedOperationException("Cannot convert ConvenioPacienteDTO to entity without a Paciente instance")
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(convenioPaciente: ConvenioPaciente?): ConvenioPacienteDTO? {
            if (convenioPaciente == null) {
                return null
            }

            return ConvenioPacienteDTO.builder()
                .id(convenioPaciente.id())
                .convenio(ConvenioDTO.fromEntity(convenioPaciente.convenio()))
                .pacienteId(convenioPaciente.paciente().pessoa().id())
                .numero(convenioPaciente.numero())
                .build()
        }

        @JvmStatic
        fun builder(): ConvenioPacienteDTOBuilder {
            return ConvenioPacienteDTOBuilder()
        }
    }

    class ConvenioPacienteDTOBuilder {
        private val instance = ConvenioPacienteDTO()

        fun id(id: Long?): ConvenioPacienteDTOBuilder {
            instance.id = id
            return this
        }

        fun convenio(convenio: ConvenioDTO?): ConvenioPacienteDTOBuilder {
            instance.convenio = convenio
            return this
        }

        fun pacienteId(pacienteId: Long?): ConvenioPacienteDTOBuilder {
            instance.pacienteId = pacienteId
            return this
        }

        fun numero(numero: String?): ConvenioPacienteDTOBuilder {
            instance.numero = numero
            return this
        }

        fun build(): ConvenioPacienteDTO {
            return instance
        }
    }
}