package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Sala
import com.clinica.atendimento.model.Terapia
import com.clinica.atendimento.model.TerapiaSala
import com.fasterxml.jackson.annotation.JsonProperty

data class TerapiaSalaDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var terapia: TerapiaDTO? = null,
    
    @JsonProperty
    var sala: SalaDTO? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): TerapiaSalaDTO {
        this.id = id
        return this
    }
    
    fun terapia(): TerapiaDTO? = terapia
    
    fun terapia(terapia: TerapiaDTO?): TerapiaSalaDTO {
        this.terapia = terapia
        return this
    }
    
    fun sala(): SalaDTO? = sala
    
    fun sala(sala: SalaDTO?): TerapiaSalaDTO {
        this.sala = sala
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): TerapiaSala {
        val terapiaEntity = terapia?.toEntity()
        val salaEntity = sala?.toEntity()

        return TerapiaSala.builder()
            .id(id)
            .terapia(terapiaEntity)
            .sala(salaEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(terapiaSala: TerapiaSala?): TerapiaSalaDTO? {
            if (terapiaSala == null) {
                return null
            }

            return TerapiaSalaDTO(
                id = terapiaSala.id(),
                terapia = TerapiaDTO.fromEntity(terapiaSala.terapia()),
                sala = SalaDTO.fromEntity(terapiaSala.sala())
            )
        }

        @JvmStatic
        fun builder(): TerapiaSalaDTOBuilder {
            return TerapiaSalaDTOBuilder()
        }
    }

    class TerapiaSalaDTOBuilder {
        private val instance = TerapiaSalaDTO()

        fun id(id: Long?): TerapiaSalaDTOBuilder {
            instance.id = id
            return this
        }

        fun terapia(terapia: TerapiaDTO?): TerapiaSalaDTOBuilder {
            instance.terapia = terapia
            return this
        }

        fun sala(sala: SalaDTO?): TerapiaSalaDTOBuilder {
            instance.sala = sala
            return this
        }

        fun build(): TerapiaSalaDTO {
            return instance
        }
    }
}