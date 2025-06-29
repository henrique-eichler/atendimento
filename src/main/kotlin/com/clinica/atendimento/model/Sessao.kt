package com.clinica.atendimento.model

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "sessao")
class Sessao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Long? = null

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    var agenda: Agenda? = null

    @Column(name = "data_inicio", nullable = false)
    var dataInicio: Instant? = null

    @Column(name = "data_termino")
    var dataTermino: Instant? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    var sala: Sala? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    var paciente: Paciente? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): Sessao {
        this.id = id
        return this
    }

    fun agenda(): Agenda? = agenda

    fun agenda(agenda: Agenda?): Sessao {
        this.agenda = agenda
        return this
    }

    fun dataInicio(): Instant? = dataInicio

    fun dataInicio(dataInicio: Instant?): Sessao {
        this.dataInicio = dataInicio
        return this
    }

    fun dataTermino(): Instant? = dataTermino

    fun dataTermino(dataTermino: Instant?): Sessao {
        this.dataTermino = dataTermino
        return this
    }

    fun sala(): Sala? = sala

    fun sala(sala: Sala?): Sessao {
        this.sala = sala
        return this
    }

    fun paciente(): Paciente? = paciente

    fun paciente(paciente: Paciente?): Sessao {
        this.paciente = paciente
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Sessao) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): SessaoBuilder {
            return SessaoBuilder()
        }
    }

    class SessaoBuilder {
        private val instance = Sessao()

        fun id(id: Long?): SessaoBuilder {
            instance.id = id
            return this
        }

        fun agenda(agenda: Agenda?): SessaoBuilder {
            instance.agenda = agenda
            return this
        }

        fun dataInicio(dataInicio: Instant?): SessaoBuilder {
            instance.dataInicio = dataInicio
            return this
        }

        fun dataTermino(dataTermino: Instant?): SessaoBuilder {
            instance.dataTermino = dataTermino
            return this
        }

        fun sala(sala: Sala?): SessaoBuilder {
            instance.sala = sala
            return this
        }

        fun paciente(paciente: Paciente?): SessaoBuilder {
            instance.paciente = paciente
            return this
        }

        fun build(): Sessao {
            return instance
        }
    }
}