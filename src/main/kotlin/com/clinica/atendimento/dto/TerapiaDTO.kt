package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Terapia
import com.fasterxml.jackson.annotation.JsonProperty

data class TerapiaDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var nome: String? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): TerapiaDTO {
        this.id = id
        return this
    }

    fun nome(nome: String?): TerapiaDTO {
        this.nome = nome
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Terapia {
        val builder = Terapia.builder()

        // Only set ID if it's not null
        id?.let { builder.id(it) }

        return builder
            .nome(nome)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(terapia: Terapia?): TerapiaDTO? {
            if (terapia == null) {
                return null
            }

            return TerapiaDTO(
                id = terapia.id,
                nome = terapia.nome
            )
        }

        @JvmStatic
        fun builder(): TerapiaDTOBuilder {
            return TerapiaDTOBuilder()
        }
    }

    class TerapiaDTOBuilder {
        private val instance = TerapiaDTO()

        fun id(id: Long?): TerapiaDTOBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): TerapiaDTOBuilder {
            instance.nome = nome
            return this
        }

        fun build(): TerapiaDTO {
            return instance
        }
    }
}