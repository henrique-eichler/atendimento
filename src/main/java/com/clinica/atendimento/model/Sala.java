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
@Table(name = "sala", uniqueConstraints = {
        @UniqueConstraint(name = "uk_sala_numvero", columnNames = {"numero"})
})
public class Sala {
    @Id
    @SequenceGenerator(name = "sala_seq", sequenceName = "sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<Cronograma> cronogramas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<TerapiaSala> terapias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<RecursoSala> recursos = new LinkedHashSet<>();
}
