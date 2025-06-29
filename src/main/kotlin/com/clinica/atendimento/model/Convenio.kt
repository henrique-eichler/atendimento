package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "convenio", uniqueConstraints = [
    UniqueConstraint(name = "uk_covenio_nome", columnNames = ["nome"])
])
class Convenio {
    @Id
    @SequenceGenerator(name = "convenio_seq", sequenceName = "convenio_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convenio_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "nome", nullable = false)
    var nome: String? = null

    @OneToMany(mappedBy = "convenio")
    var terapias: MutableSet<TerapiaConvenio> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): Convenio {
        this.id = id
        return this
    }

    fun nome(): String? = nome

    fun nome(nome: String?): Convenio {
        this.nome = nome
        return this
    }

    fun terapias(): Set<TerapiaConvenio> = terapias

    fun terapias(terapias: Set<TerapiaConvenio>): Convenio {
        this.terapias = LinkedHashSet(terapias)
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Convenio) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ConvenioBuilder {
            return ConvenioBuilder()
        }
    }

    class ConvenioBuilder {
        private val instance = Convenio()

        fun id(id: Long?): ConvenioBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): ConvenioBuilder {
            instance.nome = nome
            return this
        }

        fun terapias(terapias: Set<TerapiaConvenio>): ConvenioBuilder {
            instance.terapias = LinkedHashSet(terapias)
            return this
        }

        fun build(): Convenio {
            return instance
        }
    }
}