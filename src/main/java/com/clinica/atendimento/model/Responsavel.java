package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "responsavel")
public record Responsavel(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    Pessoa pessoa,

    @OneToMany(mappedBy = "responsavel")
    Set<ResponsavelPaciente> dependentes
) {
    // Custom constructor to initialize collections
    public Responsavel {
        dependentes = dependentes != null ? Collections.unmodifiableSet(dependentes) : Collections.emptySet();
    }

    // Constructor for JPA
    public Responsavel(Long id, Pessoa pessoa) {
        this(id, pessoa, new LinkedHashSet<>());
    }
}
