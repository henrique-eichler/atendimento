package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.SexoConverter;
import com.clinica.atendimento.model.enums.Sexo;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "pessoa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = {"email"})
})
public record Pessoa(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "nome", nullable = false)
    String nome,

    @Column(name = "email")
    String email,

    @Column(name = "data_nascimento")
    LocalDate dataNascimento,

    @Column(name = "sexo", length = 1)
    @Convert(converter = SexoConverter.class)
    Sexo sexo,

    @OneToOne(mappedBy = "pessoa")
    Paciente paciente,

    @OneToOne(mappedBy = "pessoa")
    Profissional profissional,

    @OneToOne(mappedBy = "pessoa")
    Responsavel responsavel,

    @OneToOne(mappedBy = "pessoa")
    Usuario usuario
) {
    // Constructor for JPA
    public Pessoa(Long id, String nome, String email, LocalDate dataNascimento, Sexo sexo) {
        this(id, nome, email, dataNascimento, sexo, null, null, null, null);
    }

    // Constructor for backward compatibility
    public Pessoa(Long id, String nome, String email, LocalDate dataNascimento, String sexoCodigo) {
        this(id, nome, email, dataNascimento, sexoCodigo != null ? Sexo.fromCodigo(sexoCodigo) : null, null, null, null, null);
    }
}
