package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Pessoa
import com.clinica.atendimento.model.enums.Sexo
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class PessoaDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var nome: String? = null,

    @JsonProperty
    var email: String? = null,

    @JsonProperty
    var dataNascimento: LocalDate? = null,

    @JsonProperty
    var sexo: Sexo? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): PessoaDTO {
        this.id = id
        return this
    }

    fun nome(nome: String?): PessoaDTO {
        this.nome = nome
        return this
    }

    fun email(email: String?): PessoaDTO {
        this.email = email
        return this
    }

    fun dataNascimento(dataNascimento: LocalDate?): PessoaDTO {
        this.dataNascimento = dataNascimento
        return this
    }

    fun sexo(sexo: Sexo?): PessoaDTO {
        this.sexo = sexo
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Pessoa {
        return Pessoa.builder()
            .id(id)
            .nome(nome)
            .email(email)
            .dataNascimento(dataNascimento)
            .sexo(sexo)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(pessoa: Pessoa?): PessoaDTO? {
            if (pessoa == null) {
                return null
            }

            return PessoaDTO.builder()
                .id(pessoa.id())
                .nome(pessoa.nome())
                .email(pessoa.email())
                .dataNascimento(pessoa.dataNascimento())
                .sexo(pessoa.sexo())
                .build()
        }

        @JvmStatic
        fun builder(): PessoaDTOBuilder {
            return PessoaDTOBuilder()
        }
    }

    class PessoaDTOBuilder {
        private val instance = PessoaDTO()

        fun id(id: Long?): PessoaDTOBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): PessoaDTOBuilder {
            instance.nome = nome
            return this
        }

        fun email(email: String?): PessoaDTOBuilder {
            instance.email = email
            return this
        }

        fun dataNascimento(dataNascimento: LocalDate?): PessoaDTOBuilder {
            instance.dataNascimento = dataNascimento
            return this
        }

        fun sexo(sexo: Sexo?): PessoaDTOBuilder {
            instance.sexo = sexo
            return this
        }

        fun build(): PessoaDTO {
            return instance
        }
    }
}