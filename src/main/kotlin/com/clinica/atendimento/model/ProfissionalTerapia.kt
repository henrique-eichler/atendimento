package com.clinica.atendimento.model

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "profissional_terapia", uniqueConstraints = [
    UniqueConstraint(name = "uk_profissional_terapia", columnNames = ["profissional", "terapia", "data_validade"])
])
class ProfissionalTerapia {
    @Id
    @SequenceGenerator(name = "profissional_terapia_seq", sequenceName = "profissional_terapia_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profissional_terapia_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional", nullable = false)
    var profissional: Profissional? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    var terapia: Terapia? = null

    @Column(name = "data_validade", nullable = false)
    var dataValidade: LocalDate? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): ProfissionalTerapia {
        this.id = id
        return this
    }

    fun profissional(profissional: Profissional?): ProfissionalTerapia {
        this.profissional = profissional
        return this
    }

    fun terapia(terapia: Terapia?): ProfissionalTerapia {
        this.terapia = terapia
        return this
    }

    fun dataValidade(dataValidade: LocalDate?): ProfissionalTerapia {
        this.dataValidade = dataValidade
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ProfissionalTerapia) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ProfissionalTerapiaBuilder {
            return ProfissionalTerapiaBuilder()
        }
    }

    class ProfissionalTerapiaBuilder {
        private val instance = ProfissionalTerapia()

        fun id(id: Long?): ProfissionalTerapiaBuilder {
            instance.id = id
            return this
        }

        fun profissional(profissional: Profissional?): ProfissionalTerapiaBuilder {
            instance.profissional = profissional
            return this
        }

        fun terapia(terapia: Terapia?): ProfissionalTerapiaBuilder {
            instance.terapia = terapia
            return this
        }

        fun dataValidade(dataValidade: LocalDate?): ProfissionalTerapiaBuilder {
            instance.dataValidade = dataValidade
            return this
        }

        fun build(): ProfissionalTerapia {
            return instance
        }
    }
}