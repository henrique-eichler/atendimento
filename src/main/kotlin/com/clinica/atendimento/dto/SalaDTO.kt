package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Sala
import com.fasterxml.jackson.annotation.JsonProperty

data class SalaDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var numero: Long? = null,

    @JsonProperty
    var terapias: List<TerapiaDTO>? = null,

    @JsonProperty
    var recursos: List<RecursoDTO>? = null,

    @JsonProperty
    var cronogramas: List<CronogramaDTO>? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): SalaDTO {
        this.id = id
        return this
    }

    fun numero(numero: Long?): SalaDTO {
        this.numero = numero
        return this
    }

    fun terapias(terapias: List<TerapiaDTO>?): SalaDTO {
        this.terapias = terapias
        return this
    }

    fun recursos(recursos: List<RecursoDTO>?): SalaDTO {
        this.recursos = recursos
        return this
    }

    fun cronogramas(cronogramas: List<CronogramaDTO>?): SalaDTO {
        this.cronogramas = cronogramas
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Sala {
        val builder = Sala.builder()

        // Only set ID if it's not null
        id?.let { builder.id(it) }

        return builder
            .numero(numero)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(sala: Sala?): SalaDTO? {
            if (sala == null) {
                return null
            }

            val terapiaDTOs = sala.terapias.mapNotNull { terapiaSala -> 
                TerapiaDTO.fromEntity(terapiaSala.terapia) 
            }

            val recursoDTOs = sala.recursos.mapNotNull { recursoSala -> 
                RecursoDTO.fromEntity(recursoSala.recurso) 
            }

            val cronogramaDTOs = sala.cronogramas.mapNotNull { 
                CronogramaDTO.fromEntity(it) 
            }

            return SalaDTO(
                id = sala.id,
                numero = sala.numero,
                terapias = terapiaDTOs,
                recursos = recursoDTOs,
                cronogramas = cronogramaDTOs
            )
        }

        @JvmStatic
        fun builder(): SalaDTOBuilder {
            return SalaDTOBuilder()
        }
    }

    class SalaDTOBuilder {
        private val instance = SalaDTO()

        fun id(id: Long?): SalaDTOBuilder {
            instance.id = id
            return this
        }

        fun numero(numero: Long?): SalaDTOBuilder {
            instance.numero = numero
            return this
        }

        fun terapias(terapias: List<TerapiaDTO>?): SalaDTOBuilder {
            instance.terapias = terapias
            return this
        }

        fun recursos(recursos: List<RecursoDTO>?): SalaDTOBuilder {
            instance.recursos = recursos
            return this
        }

        fun cronogramas(cronogramas: List<CronogramaDTO>?): SalaDTOBuilder {
            instance.cronogramas = cronogramas
            return this
        }

        fun build(): SalaDTO {
            return instance
        }
    }
}
