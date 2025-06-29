package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Cronograma
import com.clinica.atendimento.model.enums.DiaSemana
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class CronogramaDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var diaSemana: DiaSemana? = null,
    
    @JsonProperty
    var horaInicio: Instant? = null,
    
    @JsonProperty
    var horaTermino: Instant? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): CronogramaDTO {
        this.id = id
        return this
    }

    fun diaSemana(diaSemana: DiaSemana?): CronogramaDTO {
        this.diaSemana = diaSemana
        return this
    }

    fun horaInicio(horaInicio: Instant?): CronogramaDTO {
        this.horaInicio = horaInicio
        return this
    }

    fun horaTermino(horaTermino: Instant?): CronogramaDTO {
        this.horaTermino = horaTermino
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Cronograma {
        return Cronograma.builder()
            .id(id)
            .diaSemana(diaSemana)
            .horaInicio(horaInicio)
            .horaTermino(horaTermino)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(cronograma: Cronograma?): CronogramaDTO? {
            if (cronograma == null) {
                return null
            }

            return CronogramaDTO(
                id = cronograma.id,
                diaSemana = cronograma.diaSemana,
                horaInicio = cronograma.horaInicio,
                horaTermino = cronograma.horaTermino
            )
        }

        @JvmStatic
        fun builder(): CronogramaDTOBuilder {
            return CronogramaDTOBuilder()
        }
    }

    class CronogramaDTOBuilder {
        private val instance = CronogramaDTO()

        fun id(id: Long?): CronogramaDTOBuilder {
            instance.id = id
            return this
        }

        fun diaSemana(diaSemana: DiaSemana?): CronogramaDTOBuilder {
            instance.diaSemana = diaSemana
            return this
        }

        fun horaInicio(horaInicio: Instant?): CronogramaDTOBuilder {
            instance.horaInicio = horaInicio
            return this
        }

        fun horaTermino(horaTermino: Instant?): CronogramaDTOBuilder {
            instance.horaTermino = horaTermino
            return this
        }

        fun build(): CronogramaDTO {
            return instance
        }
    }
}