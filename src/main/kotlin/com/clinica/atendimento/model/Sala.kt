package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "sala", uniqueConstraints = [
    UniqueConstraint(name = "uk_sala_numvero", columnNames = ["numero"])
])
class Sala {
    @Id
    @SequenceGenerator(name = "sala_seq", sequenceName = "sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "numero", nullable = false)
    var numero: Long? = null

    @OneToMany(mappedBy = "sala")
    var cronogramas: MutableSet<Cronograma> = LinkedHashSet()

    @OneToMany(mappedBy = "sala")
    var terapias: MutableSet<TerapiaSala> = LinkedHashSet()

    @OneToMany(mappedBy = "sala")
    var recursos: MutableSet<RecursoSala> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Sala {
        this.id = id
        return this
    }

    fun numero(numero: Long?): Sala {
        this.numero = numero
        return this
    }

    fun cronogramas(cronogramas: MutableSet<Cronograma>): Sala {
        this.cronogramas = cronogramas
        return this
    }

    fun terapias(terapias: MutableSet<TerapiaSala>): Sala {
        this.terapias = terapias
        return this
    }

    fun recursos(recursos: MutableSet<RecursoSala>): Sala {
        this.recursos = recursos
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Sala) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): SalaBuilder {
            return SalaBuilder()
        }
    }

    class SalaBuilder {
        private val instance = Sala()

        fun id(id: Long?): SalaBuilder {
            instance.id = id
            return this
        }

        fun numero(numero: Long?): SalaBuilder {
            instance.numero = numero
            return this
        }

        fun cronogramas(cronogramas: MutableSet<Cronograma>): SalaBuilder {
            instance.cronogramas = cronogramas
            return this
        }

        fun terapias(terapias: MutableSet<TerapiaSala>): SalaBuilder {
            instance.terapias = terapias
            return this
        }

        fun recursos(recursos: MutableSet<RecursoSala>): SalaBuilder {
            instance.recursos = recursos
            return this
        }

        fun build(): Sala {
            return instance
        }
    }
}