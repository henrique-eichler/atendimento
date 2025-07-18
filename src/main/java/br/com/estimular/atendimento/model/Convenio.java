package br.com.estimular.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "convenio", uniqueConstraints = {@UniqueConstraint(name = "uk_covenio_nome", columnNames = {"nome"})})
public class Convenio {

    @Id
    @SequenceGenerator(name = "convenio_seq", sequenceName = "convenio_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convenio_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "convenio")
    @Builder.Default
    private Set<TerapiaConvenio> terapias = new LinkedHashSet<>();
}
