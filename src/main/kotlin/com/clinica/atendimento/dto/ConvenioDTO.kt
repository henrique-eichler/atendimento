package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Convenio
import com.fasterxml.jackson.annotation.JsonProperty

data class ConvenioDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var nome: String? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): ConvenioDTO {
        this.id = id
        return this
    }

    fun nome(nome: String?): ConvenioDTO {
        this.nome = nome
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Convenio {
        val builder = Convenio.builder()

        // Only set ID if it's not null
        id?.let { builder.id(it) }

        return builder
            .nome(nome)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(convenio: Convenio?): ConvenioDTO? {
            if (convenio == null) {
                return null
            }

            return ConvenioDTO.builder()
                .id(convenio.id())
                .nome(convenio.nome())
                .build()
        }

        @JvmStatic
        fun builder(): ConvenioDTOBuilder {
            return ConvenioDTOBuilder()
        }
    }

    class ConvenioDTOBuilder {
        private val instance = ConvenioDTO()

        fun id(id: Long?): ConvenioDTOBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): ConvenioDTOBuilder {
            instance.nome = nome
            return this
        }

        fun build(): ConvenioDTO {
            return instance
        }
    }
}