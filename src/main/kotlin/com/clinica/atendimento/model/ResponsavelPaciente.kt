package com.clinica.atendimento.model

import com.clinica.atendimento.model.converters.GrauParentescoConverter
import com.clinica.atendimento.model.enums.GrauParentesco
import jakarta.persistence.*

@Entity
@Table(name = "responsavel_paciente", uniqueConstraints = [
    UniqueConstraint(name = "uk_responsavel_paciente", columnNames = ["responsavel", "paciente"])
])
class ResponsavelPaciente {
    @Id
    @SequenceGenerator(name = "responsavel_paciente_seq", sequenceName = "responsavel_paciente_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "responsavel_paciente_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel", nullable = false)
    var responsavel: Responsavel? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    var paciente: Paciente? = null

    @Column(name = "grau_parentesco", nullable = false, length = 1)
    @Convert(converter = GrauParentescoConverter::class)
    var grauParentesco: GrauParentesco? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(): Long? = id

    fun id(id: Long?): ResponsavelPaciente {
        this.id = id
        return this
    }

    fun responsavel(): Responsavel? = responsavel

    fun responsavel(responsavel: Responsavel?): ResponsavelPaciente {
        this.responsavel = responsavel
        return this
    }

    fun paciente(): Paciente? = paciente

    fun paciente(paciente: Paciente?): ResponsavelPaciente {
        this.paciente = paciente
        return this
    }

    fun grauParentesco(): GrauParentesco? = grauParentesco

    fun grauParentesco(grauParentesco: GrauParentesco?): ResponsavelPaciente {
        this.grauParentesco = grauParentesco
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ResponsavelPaciente) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): ResponsavelPacienteBuilder {
            return ResponsavelPacienteBuilder()
        }
    }

    class ResponsavelPacienteBuilder {
        private val instance = ResponsavelPaciente()

        fun id(id: Long?): ResponsavelPacienteBuilder {
            instance.id = id
            return this
        }

        fun responsavel(responsavel: Responsavel?): ResponsavelPacienteBuilder {
            instance.responsavel = responsavel
            return this
        }

        fun paciente(paciente: Paciente?): ResponsavelPacienteBuilder {
            instance.paciente = paciente
            return this
        }

        fun grauParentesco(grauParentesco: GrauParentesco?): ResponsavelPacienteBuilder {
            instance.grauParentesco = grauParentesco
            return this
        }

        fun build(): ResponsavelPaciente {
            return instance
        }
    }
}