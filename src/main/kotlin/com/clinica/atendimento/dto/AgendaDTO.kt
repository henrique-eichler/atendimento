package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Agenda
import com.clinica.atendimento.model.Cronograma
import com.clinica.atendimento.model.Paciente
import com.clinica.atendimento.model.Terapia
import com.clinica.atendimento.model.Convenio
import com.clinica.atendimento.model.Profissional
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDate

data class AgendaDTO(
    @JsonProperty
    var id: Long? = null,

    @JsonProperty
    var dataAgenda: LocalDate? = null,

    @JsonProperty
    var cronograma: CronogramaDTO? = null,

    @JsonProperty
    var paciente: PacienteDTO? = null,

    @JsonProperty
    var terapia: TerapiaDTO? = null,

    @JsonProperty
    var convenio: ConvenioDTO? = null,

    @JsonProperty
    var profissional: ProfissionalDTO? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): AgendaDTO {
        this.id = id
        return this
    }

    fun dataAgenda(dataAgenda: LocalDate?): AgendaDTO {
        this.dataAgenda = dataAgenda
        return this
    }

    fun cronograma(cronograma: CronogramaDTO?): AgendaDTO {
        this.cronograma = cronograma
        return this
    }

    fun paciente(paciente: PacienteDTO?): AgendaDTO {
        this.paciente = paciente
        return this
    }

    fun terapia(terapia: TerapiaDTO?): AgendaDTO {
        this.terapia = terapia
        return this
    }

    fun convenio(convenio: ConvenioDTO?): AgendaDTO {
        this.convenio = convenio
        return this
    }

    fun profissional(profissional: ProfissionalDTO?): AgendaDTO {
        this.profissional = profissional
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Agenda {
        val cronogramaEntity = cronograma?.toEntity()
        val pacienteEntity = paciente?.toEntity()
        val terapiaEntity = terapia?.toEntity()
        val convenioEntity = convenio?.toEntity()
        val profissionalEntity = profissional?.toEntity()

        return Agenda.builder()
            .id(id)
            .dataAgenda(dataAgenda)
            .cronograma(cronogramaEntity)
            .paciente(pacienteEntity)
            .terapia(terapiaEntity)
            .convenio(convenioEntity)
            .profissional(profissionalEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(agenda: Agenda?): AgendaDTO? {
            if (agenda == null) {
                return null
            }

            return AgendaDTO.builder()
                .id(agenda.id())
                .dataAgenda(agenda.dataAgenda())
                .cronograma(CronogramaDTO.fromEntity(agenda.cronograma()))
                .paciente(PacienteDTO.fromEntity(agenda.paciente()))
                .terapia(TerapiaDTO.fromEntity(agenda.terapia()))
                .convenio(ConvenioDTO.fromEntity(agenda.convenio()))
                .profissional(ProfissionalDTO.fromEntity(agenda.profissional()))
                .build()
        }

        @JvmStatic
        fun builder(): AgendaDTOBuilder {
            return AgendaDTOBuilder()
        }
    }

    class AgendaDTOBuilder {
        private val instance = AgendaDTO()

        fun id(id: Long?): AgendaDTOBuilder {
            instance.id = id
            return this
        }

        fun dataAgenda(dataAgenda: LocalDate?): AgendaDTOBuilder {
            instance.dataAgenda = dataAgenda
            return this
        }

        fun cronograma(cronograma: CronogramaDTO?): AgendaDTOBuilder {
            instance.cronograma = cronograma
            return this
        }

        fun paciente(paciente: PacienteDTO?): AgendaDTOBuilder {
            instance.paciente = paciente
            return this
        }

        fun terapia(terapia: TerapiaDTO?): AgendaDTOBuilder {
            instance.terapia = terapia
            return this
        }

        fun convenio(convenio: ConvenioDTO?): AgendaDTOBuilder {
            instance.convenio = convenio
            return this
        }

        fun profissional(profissional: ProfissionalDTO?): AgendaDTOBuilder {
            instance.profissional = profissional
            return this
        }

        fun build(): AgendaDTO {
            return instance
        }
    }
}