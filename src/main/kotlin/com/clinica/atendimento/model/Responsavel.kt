package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "responsavel")
class Responsavel {
    @Id
    var id: Long? = null

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    var pessoa: Pessoa? = null

    @OneToMany(mappedBy = "responsavel")
    var dependentes: MutableSet<ResponsavelPaciente> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): Responsavel {
        this.id = id
        return this
    }

    fun pessoa(): Pessoa? = pessoa

    fun pessoa(pessoa: Pessoa?): Responsavel {
        this.pessoa = pessoa
        return this
    }

    fun dependentes(): Set<ResponsavelPaciente> = dependentes

    fun dependentes(dependentes: Set<ResponsavelPaciente>): Responsavel {
        this.dependentes = LinkedHashSet(dependentes)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Responsavel) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ResponsavelBuilder {
            return ResponsavelBuilder()
        }
    }

    class ResponsavelBuilder {
        private val instance = Responsavel()

        fun id(id: Long?): ResponsavelBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: Pessoa?): ResponsavelBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun dependentes(dependentes: Set<ResponsavelPaciente>): ResponsavelBuilder {
            instance.dependentes = LinkedHashSet(dependentes)
            return this
        }

        fun build(): Responsavel {
            return instance
        }
    }
}