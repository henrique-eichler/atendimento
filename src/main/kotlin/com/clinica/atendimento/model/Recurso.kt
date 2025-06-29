package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "recurso", uniqueConstraints = [
    UniqueConstraint(name = "uk_recurso_descricao", columnNames = ["nome"]),
    UniqueConstraint(name = "uk_resuro_numero_propriedade", columnNames = ["numero_propriedade"])
])
class Recurso {
    @Id
    @SequenceGenerator(name = "recurso_seq", sequenceName = "recurso_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurso_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "nome", nullable = false)
    var nome: String? = null

    @Column(name = "descricao")
    var descricao: String? = null

    @Column(name = "numero_propriedade", nullable = false)
    var numeroPropriedade: Int? = null

    @OneToMany(mappedBy = "recurso")
    var salas: MutableSet<RecursoSala> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Recurso {
        this.id = id
        return this
    }

    fun nome(nome: String?): Recurso {
        this.nome = nome
        return this
    }

    fun descricao(descricao: String?): Recurso {
        this.descricao = descricao
        return this
    }

    fun numeroPropriedade(numeroPropriedade: Int?): Recurso {
        this.numeroPropriedade = numeroPropriedade
        return this
    }

    fun salas(salas: MutableSet<RecursoSala>): Recurso {
        this.salas = salas
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Recurso) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): RecursoBuilder {
            return RecursoBuilder()
        }
    }

    class RecursoBuilder {
        private val instance = Recurso()

        fun id(id: Long?): RecursoBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): RecursoBuilder {
            instance.nome = nome
            return this
        }

        fun descricao(descricao: String?): RecursoBuilder {
            instance.descricao = descricao
            return this
        }

        fun numeroPropriedade(numeroPropriedade: Int?): RecursoBuilder {
            instance.numeroPropriedade = numeroPropriedade
            return this
        }

        fun salas(salas: MutableSet<RecursoSala>): RecursoBuilder {
            instance.salas = salas
            return this
        }

        fun build(): Recurso {
            return instance
        }
    }
}