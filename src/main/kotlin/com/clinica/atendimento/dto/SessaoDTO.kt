package com.clinica.atendimento.dto

import com.clinica.atendimento.model.Agenda
import com.clinica.atendimento.model.Paciente
import com.clinica.atendimento.model.Sala
import com.clinica.atendimento.model.Sessao
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class SessaoDTO(
    @JsonProperty
    var id: Long? = null,
    
    @JsonProperty
    var agenda: AgendaDTO? = null,
    
    @JsonProperty
    var dataInicio: Instant? = null,
    
    @JsonProperty
    var dataTermino: Instant? = null,
    
    @JsonProperty
    var sala: SalaDTO? = null,
    
    @JsonProperty
    var paciente: PacienteDTO? = null
) {
    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id
    
    fun id(id: Long?): SessaoDTO {
        this.id = id
        return this
    }
    
    fun agenda(): AgendaDTO? = agenda
    
    fun agenda(agenda: AgendaDTO?): SessaoDTO {
        this.agenda = agenda
        return this
    }
    
    fun dataInicio(): Instant? = dataInicio
    
    fun dataInicio(dataInicio: Instant?): SessaoDTO {
        this.dataInicio = dataInicio
        return this
    }
    
    fun dataTermino(): Instant? = dataTermino
    
    fun dataTermino(dataTermino: Instant?): SessaoDTO {
        this.dataTermino = dataTermino
        return this
    }
    
    fun sala(): SalaDTO? = sala
    
    fun sala(sala: SalaDTO?): SessaoDTO {
        this.sala = sala
        return this
    }
    
    fun paciente(): PacienteDTO? = paciente
    
    fun paciente(paciente: PacienteDTO?): SessaoDTO {
        this.paciente = paciente
        return this
    }

    // Method to convert from DTO to entity
    fun toEntity(): Sessao {
        val agendaEntity = agenda?.toEntity()
        val salaEntity = sala?.toEntity()
        val pacienteEntity = paciente?.toEntity()

        return Sessao.builder()
            .id(id)
            .agenda(agendaEntity)
            .dataInicio(dataInicio)
            .dataTermino(dataTermino)
            .sala(salaEntity)
            .paciente(pacienteEntity)
            .build()
    }

    companion object {
        // Static method to convert from entity to DTO
        @JvmStatic
        fun fromEntity(sessao: Sessao?): SessaoDTO? {
            if (sessao == null) {
                return null
            }

            return SessaoDTO(
                id = sessao.id(),
                agenda = AgendaDTO.fromEntity(sessao.agenda()),
                dataInicio = sessao.dataInicio(),
                dataTermino = sessao.dataTermino(),
                sala = SalaDTO.fromEntity(sessao.sala()),
                paciente = PacienteDTO.fromEntity(sessao.paciente())
            )
        }

        @JvmStatic
        fun builder(): SessaoDTOBuilder {
            return SessaoDTOBuilder()
        }
    }

    class SessaoDTOBuilder {
        private val instance = SessaoDTO()

        fun id(id: Long?): SessaoDTOBuilder {
            instance.id = id
            return this
        }

        fun agenda(agenda: AgendaDTO?): SessaoDTOBuilder {
            instance.agenda = agenda
            return this
        }

        fun dataInicio(dataInicio: Instant?): SessaoDTOBuilder {
            instance.dataInicio = dataInicio
            return this
        }

        fun dataTermino(dataTermino: Instant?): SessaoDTOBuilder {
            instance.dataTermino = dataTermino
            return this
        }

        fun sala(sala: SalaDTO?): SessaoDTOBuilder {
            instance.sala = sala
            return this
        }

        fun paciente(paciente: PacienteDTO?): SessaoDTOBuilder {
            instance.paciente = paciente
            return this
        }

        fun build(): SessaoDTO {
            return instance
        }
    }
}