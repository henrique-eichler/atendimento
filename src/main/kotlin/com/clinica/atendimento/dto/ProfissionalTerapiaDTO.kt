package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Profissional
import com.clinica.atendimento.model.ProfissionalTerapia
import com.clinica.atendimento.model.Terapia
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class ProfissionalTerapiaDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var profissional: ProfissionalDTO? = null,
    
    @JsonProperty
    var terapia: TerapiaDTO? = null,
    
    @JsonProperty
    var dataValidade: LocalDate? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): ProfissionalTerapiaDTO {
        this.id = id
        return this
    }
    
    fun profissional(): ProfissionalDTO? = profissional
    
    fun profissional(profissional: ProfissionalDTO?): ProfissionalTerapiaDTO {
        this.profissional = profissional
        return this
    }
    
    fun terapia(): TerapiaDTO? = terapia
    
    fun terapia(terapia: TerapiaDTO?): ProfissionalTerapiaDTO {
        this.terapia = terapia
        return this
    }
    
    fun dataValidade(): LocalDate? = dataValidade
    
    fun dataValidade(dataValidade: LocalDate?): ProfissionalTerapiaDTO {
        this.dataValidade = dataValidade
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): ProfissionalTerapia {
        val profissionalEntity = profissional?.toEntity()
        val terapiaEntity = terapia?.toEntity()

        return ProfissionalTerapia.builder()
            .id(id)
            .profissional(profissionalEntity)
            .terapia(terapiaEntity)
            .dataValidade(dataValidade)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(profissionalTerapia: ProfissionalTerapia?): ProfissionalTerapiaDTO? {
            if (profissionalTerapia == null) {
                return null
            }

            return ProfissionalTerapiaDTO(
                id = profissionalTerapia.id(),
                profissional = ProfissionalDTO.fromEntity(profissionalTerapia.profissional()),
                terapia = TerapiaDTO.fromEntity(profissionalTerapia.terapia()),
                dataValidade = profissionalTerapia.dataValidade()
            )
        }

        @JvmStatic
        fun builder(): ProfissionalTerapiaDTOBuilder {
            return ProfissionalTerapiaDTOBuilder()
        }
    }

    class ProfissionalTerapiaDTOBuilder {
        private val instance = ProfissionalTerapiaDTO()

        fun id(id: Long?): ProfissionalTerapiaDTOBuilder {
            instance.id = id
            return this
        }

        fun profissional(profissional: ProfissionalDTO?): ProfissionalTerapiaDTOBuilder {
            instance.profissional = profissional
            return this
        }

        fun terapia(terapia: TerapiaDTO?): ProfissionalTerapiaDTOBuilder {
            instance.terapia = terapia
            return this
        }

        fun dataValidade(dataValidade: LocalDate?): ProfissionalTerapiaDTOBuilder {
            instance.dataValidade = dataValidade
            return this
        }

        fun build(): ProfissionalTerapiaDTO {
            return instance
        }
    }
}