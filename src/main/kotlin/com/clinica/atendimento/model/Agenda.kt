package com.clinica.atendimento.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "agenda")
class Agenda {
    @Id
    @SequenceGenerator(name = "agenda_seq", sequenceName = "agenda_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "agenda_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "data_agenda", nullable = false)
    var dataAgenda: LocalDate? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cronograma", nullable = false)
    var cronograma: Cronograma? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    var paciente: Paciente? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    var terapia: Terapia? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    var convenio: Convenio? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional", nullable = false)
    var profissional: Profissional? = null

    @OneToOne(mappedBy = "agenda")
    var sessao: Sessao? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): Agenda {
        this.id = id
        return this
    }

    fun dataAgenda(): LocalDate? = dataAgenda

    fun dataAgenda(dataAgenda: LocalDate?): Agenda {
        this.dataAgenda = dataAgenda
        return this
    }

    fun cronograma(): Cronograma? = cronograma

    fun cronograma(cronograma: Cronograma?): Agenda {
        this.cronograma = cronograma
        return this
    }

    fun paciente(): Paciente? = paciente

    fun paciente(paciente: Paciente?): Agenda {
        this.paciente = paciente
        return this
    }

    fun terapia(): Terapia? = terapia

    fun terapia(terapia: Terapia?): Agenda {
        this.terapia = terapia
        return this
    }

    fun convenio(): Convenio? = convenio

    fun convenio(convenio: Convenio?): Agenda {
        this.convenio = convenio
        return this
    }

    fun profissional(): Profissional? = profissional

    fun profissional(profissional: Profissional?): Agenda {
        this.profissional = profissional
        return this
    }

    fun sessao(): Sessao? = sessao

    fun sessao(sessao: Sessao?): Agenda {
        this.sessao = sessao
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Agenda) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): AgendaBuilder {
            return AgendaBuilder()
        }
    }

    class AgendaBuilder {
        private val instance = Agenda()

        fun id(id: Long?): AgendaBuilder {
            instance.id = id
            return this
        }

        fun dataAgenda(dataAgenda: LocalDate?): AgendaBuilder {
            instance.dataAgenda = dataAgenda
            return this
        }

        fun cronograma(cronograma: Cronograma?): AgendaBuilder {
            instance.cronograma = cronograma
            return this
        }

        fun paciente(paciente: Paciente?): AgendaBuilder {
            instance.paciente = paciente
            return this
        }

        fun terapia(terapia: Terapia?): AgendaBuilder {
            instance.terapia = terapia
            return this
        }

        fun convenio(convenio: Convenio?): AgendaBuilder {
            instance.convenio = convenio
            return this
        }

        fun profissional(profissional: Profissional?): AgendaBuilder {
            instance.profissional = profissional
            return this
        }

        fun sessao(sessao: Sessao?): AgendaBuilder {
            instance.sessao = sessao
            return this
        }

        fun build(): Agenda {
            return instance
        }
    }
}