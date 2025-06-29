package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Recurso
import com.fasterxml.jackson.annotation.JsonProperty

data class RecursoDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var nome: String? = null,
    
    @JsonProperty
    var descricao: String? = null,
    
    @JsonProperty
    var numeroPropriedade: Int? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): RecursoDTO {
        this.id = id
        return this
    }

    fun nome(nome: String?): RecursoDTO {
        this.nome = nome
        return this
    }

    fun descricao(descricao: String?): RecursoDTO {
        this.descricao = descricao
        return this
    }

    fun numeroPropriedade(numeroPropriedade: Int?): RecursoDTO {
        this.numeroPropriedade = numeroPropriedade
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Recurso {
        val recurso = Recurso()
        recurso.id(id)
        recurso.nome(nome)
        recurso.descricao(descricao)
        recurso.numeroPropriedade(numeroPropriedade)
        return recurso
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(recurso: Recurso?): RecursoDTO? {
            if (recurso == null) {
                return null
            }

            return RecursoDTO(
                id = recurso.id,
                nome = recurso.nome,
                descricao = recurso.descricao,
                numeroPropriedade = recurso.numeroPropriedade
            )
        }

        @JvmStatic
        fun builder(): RecursoDTOBuilder {
            return RecursoDTOBuilder()
        }
    }

    class RecursoDTOBuilder {
        private val instance = RecursoDTO()

        fun id(id: Long?): RecursoDTOBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): RecursoDTOBuilder {
            instance.nome = nome
            return this
        }

        fun descricao(descricao: String?): RecursoDTOBuilder {
            instance.descricao = descricao
            return this
        }

        fun numeroPropriedade(numeroPropriedade: Int?): RecursoDTOBuilder {
            instance.numeroPropriedade = numeroPropriedade
            return this
        }

        fun build(): RecursoDTO {
            return instance
        }
    }
}