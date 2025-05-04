package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.SexoConverter;
import com.clinica.atendimento.model.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "pessoa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = {"email"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class Pessoa {

    @Id
    @SequenceGenerator(name = "pessoa_seq", sequenceName = "pessoa_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pessoa_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "email")
    private String email;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "sexo", length = 1)
    @Convert(converter = SexoConverter.class)
    private Sexo sexo;

    @OneToOne(mappedBy = "pessoa")
    private Paciente paciente;

    @OneToOne(mappedBy = "pessoa")
    private Profissional profissional;

    @OneToOne(mappedBy = "pessoa")
    private Responsavel responsavel;

    // Static factory method for backward compatibility
    public static Pessoa fromSexoCodigo(Long id, String nome, String email, LocalDate dataNascimento, String sexoCodigo) {
        return Pessoa.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .dataNascimento(dataNascimento)
                .sexo(sexoCodigo != null ? Sexo.fromCodigo(sexoCodigo) : null)
                .build();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Pessoa pessoa = (Pessoa) o;
        return id != null && Objects.equals(id, pessoa.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
