package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "paciente")
class Paciente {
    @Id
    var id: Long? = null

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    var pessoa: Pessoa? = null

    @OneToMany(mappedBy = "paciente")
    var convenios: MutableSet<ConvenioPaciente> = LinkedHashSet()

    @OneToMany(mappedBy = "paciente")
    var responsaveis: MutableSet<ResponsavelPaciente> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): Paciente {
        this.id = id
        return this
    }

    fun pessoa(): Pessoa? = pessoa

    fun pessoa(pessoa: Pessoa?): Paciente {
        this.pessoa = pessoa
        return this
    }

    fun convenios(): Set<ConvenioPaciente> = convenios

    fun convenios(convenios: Set<ConvenioPaciente>): Paciente {
        this.convenios = LinkedHashSet(convenios)
        return this
    }

    fun responsaveis(): Set<ResponsavelPaciente> = responsaveis

    fun responsaveis(responsaveis: Set<ResponsavelPaciente>): Paciente {
        this.responsaveis = LinkedHashSet(responsaveis)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Paciente) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): PacienteBuilder {
            return PacienteBuilder()
        }
    }

    class PacienteBuilder {
        private val instance = Paciente()

        fun id(id: Long?): PacienteBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: Pessoa?): PacienteBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun convenios(convenios: Set<ConvenioPaciente>): PacienteBuilder {
            instance.convenios = LinkedHashSet(convenios)
            return this
        }

        fun responsaveis(responsaveis: Set<ResponsavelPaciente>): PacienteBuilder {
            instance.responsaveis = LinkedHashSet(responsaveis)
            return this
        }

        fun build(): Paciente {
            return instance
        }
    }
}