package com.clinica.atendimento.model

import jakarta.persistence.*
import java.util.LinkedHashSet

@Entity
@Table(name = "profissional")
class Profissional {
    @Id
    var id: Long? = null

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    var pessoa: Pessoa? = null

    @OneToMany(mappedBy = "profissional")
    var terapias: MutableSet<ProfissionalTerapia> = LinkedHashSet()

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Profissional {
        this.id = id
        return this
    }

    fun pessoa(pessoa: Pessoa?): Profissional {
        this.pessoa = pessoa
        return this
    }

    fun terapias(terapias: MutableSet<ProfissionalTerapia>): Profissional {
        this.terapias = terapias
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Profissional) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ProfissionalBuilder {
            return ProfissionalBuilder()
        }
    }

    class ProfissionalBuilder {
        private val instance = Profissional()

        fun id(id: Long?): ProfissionalBuilder {
            instance.id = id
            return this
        }

        fun pessoa(pessoa: Pessoa?): ProfissionalBuilder {
            instance.pessoa = pessoa
            return this
        }

        fun terapias(terapias: MutableSet<ProfissionalTerapia>): ProfissionalBuilder {
            instance.terapias = terapias
            return this
        }

        fun build(): Profissional {
            return instance
        }
    }
}