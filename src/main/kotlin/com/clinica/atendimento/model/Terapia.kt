package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "terapia", uniqueConstraints = [
    UniqueConstraint(name = "uk_terapia_nome", columnNames = ["nome"])
])
class Terapia {
    @Id
    @SequenceGenerator(name = "terapia_seq", sequenceName = "terapia_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "nome", nullable = false)
    var nome: String? = null

    @OneToMany(mappedBy = "terapia")
    var profissionais: MutableSet<ProfissionalTerapia> = LinkedHashSet()

    @OneToMany(mappedBy = "terapia")
    var convenios: MutableSet<TerapiaConvenio> = LinkedHashSet()

    @OneToMany(mappedBy = "terapia")
    var salas: MutableSet<TerapiaSala> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Terapia {
        this.id = id
        return this
    }

    fun nome(nome: String?): Terapia {
        this.nome = nome
        return this
    }

    fun profissionais(profissionais: MutableSet<ProfissionalTerapia>): Terapia {
        this.profissionais = profissionais
        return this
    }

    fun convenios(convenios: MutableSet<TerapiaConvenio>): Terapia {
        this.convenios = convenios
        return this
    }

    fun salas(salas: MutableSet<TerapiaSala>): Terapia {
        this.salas = salas
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Terapia) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): TerapiaBuilder {
            return TerapiaBuilder()
        }
    }

    class TerapiaBuilder {
        private val instance = Terapia()

        fun id(id: Long?): TerapiaBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): TerapiaBuilder {
            instance.nome = nome
            return this
        }

        fun profissionais(profissionais: MutableSet<ProfissionalTerapia>): TerapiaBuilder {
            instance.profissionais = profissionais
            return this
        }

        fun convenios(convenios: MutableSet<TerapiaConvenio>): TerapiaBuilder {
            instance.convenios = convenios
            return this
        }

        fun salas(salas: MutableSet<TerapiaSala>): TerapiaBuilder {
            instance.salas = salas
            return this
        }

        fun build(): Terapia {
            return instance
        }
    }
}