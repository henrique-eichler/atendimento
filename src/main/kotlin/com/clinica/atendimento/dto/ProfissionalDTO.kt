package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Pessoa
import com.clinica.atendimento.model.Profissional
import com.fasterxml.jackson.annotation.JsonProperty

data class ProfissionalDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var pessoa: PessoaDTO? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): ProfissionalDTO {
        this.id = id
        return this
    }
    
    fun pessoa(): PessoaDTO? = pessoa
    
    fun pessoa(pessoa: PessoaDTO?): ProfissionalDTO {
        this.pessoa = pessoa
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Profissional {
        val pessoaEntity = pessoa?.toEntity()

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id)
        }

        return Profissional.builder()
            .pessoa(pessoaEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(profissional: Profissional?): ProfissionalDTO? {
            if (profissional == null) {
                return null
            }

            return builder()
                .id(profissional.pessoa().id())
                .pessoa(PessoaDTO.fromEntity(profissional.pessoa()))
                .build()
        }

        @JvmStatic
        fun builder(): ProfissionalDTOBuilder {
            return ProfissionalDTOBuilder()
        }
    }

    class ProfissionalDTOBuilder {
        private val instance = ProfissionalDTO()

        fun id(id: Long?): ProfissionalDTOBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: PessoaDTO?): ProfissionalDTOBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun build(): ProfissionalDTO {
            return instance
        }
    }
}