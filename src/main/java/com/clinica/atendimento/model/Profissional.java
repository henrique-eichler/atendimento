package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "profissional")
public record Profissional(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    Pessoa pessoa,

    @OneToMany(mappedBy = "profissional")
    Set<ProfissionalTerapia> terapias
) {
    // Custom constructor to initialize collections
    public Profissional {
        terapias = terapias != null ? Collections.unmodifiableSet(terapias) : Collections.emptySet();
    }

    // Constructor for JPA
    public Profissional(Long id, Pessoa pessoa) {
        this(id, pessoa, new LinkedHashSet<>());
    }
}
