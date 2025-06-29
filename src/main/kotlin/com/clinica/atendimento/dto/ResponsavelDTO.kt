package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Pessoa
import com.clinica.atendimento.model.Responsavel
import com.fasterxml.jackson.annotation.JsonProperty

data class ResponsavelDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var pessoa: PessoaDTO? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): ResponsavelDTO {
        this.id = id
        return this
    }
    
    fun pessoa(): PessoaDTO? = pessoa
    
    fun pessoa(pessoa: PessoaDTO?): ResponsavelDTO {
        this.pessoa = pessoa
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Responsavel {
        val pessoaEntity = pessoa?.toEntity()

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id)
        }

        return Responsavel.builder()
            .pessoa(pessoaEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(responsavel: Responsavel?): ResponsavelDTO? {
            if (responsavel == null) {
                return null
            }

            return ResponsavelDTO(
                id = responsavel.pessoa().id(),
                pessoa = PessoaDTO.fromEntity(responsavel.pessoa())
            )
        }

        @JvmStatic
        fun builder(): ResponsavelDTOBuilder {
            return ResponsavelDTOBuilder()
        }
    }

    class ResponsavelDTOBuilder {
        private val instance = ResponsavelDTO()

        fun id(id: Long?): ResponsavelDTOBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: PessoaDTO?): ResponsavelDTOBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun build(): ResponsavelDTO {
            return instance
        }
    }
}