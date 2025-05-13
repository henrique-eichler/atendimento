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
@Table(name = "profissional")
public class Profissional {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    private Pessoa pessoa;

    @OneToMany(mappedBy = "profissional")
    @Builder.Default
    private Set<ProfissionalTerapia> terapias = new LinkedHashSet<>();
}
