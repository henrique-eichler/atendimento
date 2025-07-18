package br.com.estimular.atendimento.model;

import br.com.estimular.atendimento.model.converters.SexoConverter;
import br.com.estimular.atendimento.model.enums.Sexo;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "pessoa", uniqueConstraints = {@UniqueConstraint(name = "uk_usuario_email", columnNames = {"email"})})
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
}
