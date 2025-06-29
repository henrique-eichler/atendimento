package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Paciente
import com.clinica.atendimento.model.Pessoa
import com.fasterxml.jackson.annotation.JsonProperty

data class PacienteDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var pessoa: PessoaDTO? = null,

    @JsonProperty
    var convenios: Set<ConvenioPacienteDTO>? = null,

    @JsonProperty
    var responsaveis: Set<ResponsavelPacienteDTO>? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): PacienteDTO {
        this.id = id
        return this
    }

    fun pessoa(): PessoaDTO? = pessoa

    fun pessoa(pessoa: PessoaDTO?): PacienteDTO {
        this.pessoa = pessoa
        return this
    }

    fun convenios(): Set<ConvenioPacienteDTO>? = convenios

    fun convenios(convenios: Set<ConvenioPacienteDTO>?): PacienteDTO {
        this.convenios = convenios
        return this
    }

    fun responsaveis(): Set<ResponsavelPacienteDTO>? = responsaveis

    fun responsaveis(responsaveis: Set<ResponsavelPacienteDTO>?): PacienteDTO {
        this.responsaveis = responsaveis
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Paciente {
        val pessoaEntity = pessoa?.toEntity()

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id)
        }

        // We need to create the Paciente first, then create the relationships
        // This is because of the circular dependency between Paciente and its relationships
        return Paciente.builder()
            .pessoa(pessoaEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(paciente: Paciente?): PacienteDTO? {
            if (paciente == null) {
                return null
            }

            return builder()
                .id(paciente.pessoa().id())
                .pessoa(PessoaDTO.fromEntity(paciente.pessoa()))
                .convenios(paciente.convenios().mapNotNull { ConvenioPacienteDTO.fromEntity(it) }.toSet())
                .responsaveis(paciente.responsaveis().mapNotNull { ResponsavelPacienteDTO.fromEntity(it) }.toSet())
                .build()
        }

        @JvmStatic
        fun builder(): PacienteDTOBuilder {
            return PacienteDTOBuilder()
        }
    }

    class PacienteDTOBuilder {
        private val instance = PacienteDTO()

        fun id(id: Long?): PacienteDTOBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: PessoaDTO?): PacienteDTOBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun convenios(convenios: Set<ConvenioPacienteDTO>?): PacienteDTOBuilder {
            instance.convenios = convenios
            return this
        }

        fun responsaveis(responsaveis: Set<ResponsavelPacienteDTO>?): PacienteDTOBuilder {
            instance.responsaveis = responsaveis
            return this
        }

        fun build(): PacienteDTO {
            return instance
        }
    }
}
