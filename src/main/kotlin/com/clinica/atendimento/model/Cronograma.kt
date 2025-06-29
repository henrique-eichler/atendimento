package com.clinica.atendimento.model

import com.clinica.atendimento.model.converters.DiaSemanaConverter
import com.clinica.atendimento.model.enums.DiaSemana
import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "cronograma", uniqueConstraints = [
    UniqueConstraint(name = "uk_cronograma", columnNames = ["sala", "dia_semana", "hora_inicio"])
])
class Cronograma {
    @Id
    @SequenceGenerator(name = "cronograma_seq", sequenceName = "cronograma_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cronograma_seq")
    @Column(name = "id")
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    var sala: Sala? = null

    @Column(name = "dia_semana", nullable = false, length = 1)
    @Convert(converter = DiaSemanaConverter::class)
    var diaSemana: DiaSemana? = null

    @Column(name = "hora_inicio", nullable = false)
    var horaInicio: Instant? = null

    @Column(name = "hora_termino", nullable = false)
    var horaTermino: Instant? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Cronograma {
        this.id = id
        return this
    }

    fun sala(sala: Sala?): Cronograma {
        this.sala = sala
        return this
    }

    fun diaSemana(diaSemana: DiaSemana?): Cronograma {
        this.diaSemana = diaSemana
        return this
    }

    fun horaInicio(horaInicio: Instant?): Cronograma {
        this.horaInicio = horaInicio
        return this
    }

    fun horaTermino(horaTermino: Instant?): Cronograma {
        this.horaTermino = horaTermino
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cronograma) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): CronogramaBuilder {
            return CronogramaBuilder()
        }
    }

    class CronogramaBuilder {
        private val instance = Cronograma()

        fun id(id: Long?): CronogramaBuilder {
            instance.id = id
            return this
        }

        fun sala(sala: Sala?): CronogramaBuilder {
            instance.sala = sala
            return this
        }

        fun diaSemana(diaSemana: DiaSemana?): CronogramaBuilder {
            instance.diaSemana = diaSemana
            return this
        }

        fun horaInicio(horaInicio: Instant?): CronogramaBuilder {
            instance.horaInicio = horaInicio
            return this
        }

        fun horaTermino(horaTermino: Instant?): CronogramaBuilder {
            instance.horaTermino = horaTermino
            return this
        }

        fun build(): Cronograma {
            return instance
        }
    }
}