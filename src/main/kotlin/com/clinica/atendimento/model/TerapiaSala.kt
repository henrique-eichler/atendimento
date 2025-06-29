package com.clinica.atendimento.model

import jakarta.persistence.*

@Entity
@Table(name = "terapia_sala", uniqueConstraints = [
    UniqueConstraint(name = "uk_terapia_sala", columnNames = ["terapia", "sala"])
])
class TerapiaSala {
    @Id
    @SequenceGenerator(name = "terapia_sala_seq", sequenceName = "terapia_sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_sala_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    var terapia: Terapia? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    var sala: Sala? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): TerapiaSala {
        this.id = id
        return this
    }

    fun terapia(terapia: Terapia?): TerapiaSala {
        this.terapia = terapia
        return this
    }

    fun sala(sala: Sala?): TerapiaSala {
        this.sala = sala
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TerapiaSala) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): TerapiaSalaBuilder {
            return TerapiaSalaBuilder()
        }
    }

    class TerapiaSalaBuilder {
        private val instance = TerapiaSala()

        fun id(id: Long?): TerapiaSalaBuilder {
            instance.id = id
            return this
        }

        fun terapia(terapia: Terapia?): TerapiaSalaBuilder {
            instance.terapia = terapia
            return this
        }

        fun sala(sala: Sala?): TerapiaSalaBuilder {
            instance.sala = sala
            return this
        }

        fun build(): TerapiaSala {
            return instance
        }
    }
}