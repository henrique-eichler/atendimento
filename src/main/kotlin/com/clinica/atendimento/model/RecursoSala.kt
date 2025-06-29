package com.clinica.atendimento.model

import jakarta.persistence.*

@Entity
@Table(name = "recurso_sala")
class RecursoSala {
    @Id
    @SequenceGenerator(name = "recurso_sala_seq", sequenceName = "recurso_sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurso_sala_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso", nullable = false)
    var recurso: Recurso? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    var sala: Sala? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): RecursoSala {
        this.id = id
        return this
    }

    fun recurso(recurso: Recurso?): RecursoSala {
        this.recurso = recurso
        return this
    }

    fun sala(sala: Sala?): RecursoSala {
        this.sala = sala
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecursoSala) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): RecursoSalaBuilder {
            return RecursoSalaBuilder()
        }
    }

    class RecursoSalaBuilder {
        private val instance = RecursoSala()

        fun id(id: Long?): RecursoSalaBuilder {
            instance.id = id
            return this
        }

        fun recurso(recurso: Recurso?): RecursoSalaBuilder {
            instance.recurso = recurso
            return this
        }

        fun sala(sala: Sala?): RecursoSalaBuilder {
            instance.sala = sala
            return this
        }

        fun build(): RecursoSala {
            return instance
        }
    }
}