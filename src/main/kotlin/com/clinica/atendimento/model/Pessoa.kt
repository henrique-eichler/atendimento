package com.clinica.atendimento.model

import com.clinica.atendimento.model.converters.SexoConverter
import com.clinica.atendimento.model.enums.Sexo
import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "pessoa", uniqueConstraints = [
    UniqueConstraint(name = "uk_usuario_email", columnNames = ["email"])
])
class Pessoa {
    @Id
    @SequenceGenerator(name = "pessoa_seq", sequenceName = "pessoa_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_seq")
    @Column(name = "id")
    var id: Long? = null

    @Column(name = "nome", nullable = false)
    var nome: String? = null

    @Column(name = "email")
    var email: String? = null

    @Column(name = "data_nascimento")
    var dataNascimento: LocalDate? = null

    @Column(name = "sexo", length = 1)
    @Convert(converter = SexoConverter::class)
    var sexo: Sexo? = null

    @OneToOne(mappedBy = "pessoa")
    var paciente: Paciente? = null

    @OneToOne(mappedBy = "pessoa")
    var profissional: Profissional? = null

    @OneToOne(mappedBy = "pessoa")
    var responsavel: Responsavel? = null

    // Fluent accessors to maintain compatibility with Java code
    fun id(id: Long?): Pessoa {
        this.id = id
        return this
    }

    fun nome(nome: String?): Pessoa {
        this.nome = nome
        return this
    }

    fun email(email: String?): Pessoa {
        this.email = email
        return this
    }

    fun dataNascimento(dataNascimento: LocalDate?): Pessoa {
        this.dataNascimento = dataNascimento
        return this
    }

    fun sexo(sexo: Sexo?): Pessoa {
        this.sexo = sexo
        return this
    }

    fun paciente(paciente: Paciente?): Pessoa {
        this.paciente = paciente
        return this
    }

    fun profissional(profissional: Profissional?): Pessoa {
        this.profissional = profissional
        return this
    }

    fun responsavel(responsavel: Responsavel?): Pessoa {
        this.responsavel = responsavel
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pessoa) return false
        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        @JvmStatic
        fun builder(): PessoaBuilder {
            return PessoaBuilder()
        }
    }

    class PessoaBuilder {
        private val instance = Pessoa()

        fun id(id: Long?): PessoaBuilder {
            instance.id = id
            return this
        }

        fun nome(nome: String?): PessoaBuilder {
            instance.nome = nome
            return this
        }

        fun email(email: String?): PessoaBuilder {
            instance.email = email
            return this
        }

        fun dataNascimento(dataNascimento: LocalDate?): PessoaBuilder {
            instance.dataNascimento = dataNascimento
            return this
        }

        fun sexo(sexo: Sexo?): PessoaBuilder {
            instance.sexo = sexo
            return this
        }

        fun paciente(paciente: Paciente?): PessoaBuilder {
            instance.paciente = paciente
            return this
        }

        fun profissional(profissional: Profissional?): PessoaBuilder {
            instance.profissional = profissional
            return this
        }

        fun responsavel(responsavel: Responsavel?): PessoaBuilder {
            instance.responsavel = responsavel
            return this
        }

        fun build(): Pessoa {
            return instance
        }
    }
}