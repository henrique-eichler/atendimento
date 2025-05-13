package com.clinica.atendimento.model;

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
@Table(name = "terapia", uniqueConstraints = {@UniqueConstraint(name = "uk_terapia_nome", columnNames = {"nome"})})
public class Terapia {
    @Id
    @SequenceGenerator(name = "terapia_seq", sequenceName = "terapia_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<ProfissionalTerapia> profissionais = new LinkedHashSet<>();

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<TerapiaConvenio> convenios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<TerapiaSala> salas = new LinkedHashSet<>();
}
