package com.clinica.atendimento.model

import jakarta.persistence.*

@Entity
@Table(name = "convenio_paciente", uniqueConstraints = [
    UniqueConstraint(name = "uk_convenio_paciente_paciente", columnNames = ["convenio", "paciente"]),
    UniqueConstraint(name = "uk_convenio_paciente_numero", columnNames = ["convenio", "numero"])
])
class ConvenioPaciente {
    @Id
    @SequenceGenerator(name = "convenio_paciente_seq", sequenceName = "convenio_paciente_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convenio_paciente_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    var convenio: Convenio? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    var paciente: Paciente? = null

    @Column(name = "numero", nullable = false, length = 100)
    var numero: String? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): ConvenioPaciente {
        this.id = id
        return this
    }

    fun convenio(): Convenio? = convenio

    fun convenio(convenio: Convenio?): ConvenioPaciente {
        this.convenio = convenio
        return this
    }

    fun paciente(): Paciente? = paciente

    fun paciente(paciente: Paciente?): ConvenioPaciente {
        this.paciente = paciente
        return this
    }

    fun numero(): String? = numero

    fun numero(numero: String?): ConvenioPaciente {
        this.numero = numero
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ConvenioPaciente) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ConvenioPacienteBuilder {
            return ConvenioPacienteBuilder()
        }
    }

    class ConvenioPacienteBuilder {
        private val instance = ConvenioPaciente()

        fun id(id: Long?): ConvenioPacienteBuilder {
            instance.id = id
            return this
        }

        fun convenio(convenio: Convenio?): ConvenioPacienteBuilder {
            instance.convenio = convenio
            return this
        }

        fun paciente(paciente: Paciente?): ConvenioPacienteBuilder {
            instance.paciente = paciente
            return this
        }

        fun numero(numero: String?): ConvenioPacienteBuilder {
            instance.numero = numero
            return this
        }

        fun build(): ConvenioPaciente {
            return instance
        }
    }
}