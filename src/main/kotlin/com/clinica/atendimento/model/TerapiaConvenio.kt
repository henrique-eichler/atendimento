package com.clinica.atendimento.model

import jakarta.persistence.*

@Entity
@Table(name = "terapia_convenio", uniqueConstraints = [
    UniqueConstraint(name = "uk_terapia_convenio", columnNames = ["terapia", "convenio"])
])
class TerapiaConvenio {
    @Id
    @SequenceGenerator(name = "terapia_convenio_seq", sequenceName = "terapia_convenio_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_convenio_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    var terapia: Terapia? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    var convenio: Convenio? = null

    @Column(name = "valor_terapia", nullable = false)
    var valorTerapia: Float? = null

    @Column(name = "valor_profissional", nullable = false)
    var valorProfissional: Float? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): TerapiaConvenio {
        this.id = id
        return this
    }

    fun terapia(): Terapia? = terapia

    fun terapia(terapia: Terapia?): TerapiaConvenio {
        this.terapia = terapia
        return this
    }

    fun convenio(): Convenio? = convenio

    fun convenio(convenio: Convenio?): TerapiaConvenio {
        this.convenio = convenio
        return this
    }

    fun valorTerapia(): Float? = valorTerapia

    fun valorTerapia(valorTerapia: Float?): TerapiaConvenio {
        this.valorTerapia = valorTerapia
        return this
    }

    fun valorProfissional(): Float? = valorProfissional

    fun valorProfissional(valorProfissional: Float?): TerapiaConvenio {
        this.valorProfissional = valorProfissional
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TerapiaConvenio) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): TerapiaConvenioBuilder {
            return TerapiaConvenioBuilder()
        }
    }

    class TerapiaConvenioBuilder {
        private val instance = TerapiaConvenio()

        fun id(id: Long?): TerapiaConvenioBuilder {
            instance.id = id
            return this
        }

        fun terapia(terapia: Terapia?): TerapiaConvenioBuilder {
            instance.terapia = terapia
            return this
        }

        fun convenio(convenio: Convenio?): TerapiaConvenioBuilder {
            instance.convenio = convenio
            return this
        }

        fun valorTerapia(valorTerapia: Float?): TerapiaConvenioBuilder {
            instance.valorTerapia = valorTerapia
            return this
        }

        fun valorProfissional(valorProfissional: Float?): TerapiaConvenioBuilder {
            instance.valorProfissional = valorProfissional
            return this
        }

        fun build(): TerapiaConvenio {
            return instance
        }
    }
}