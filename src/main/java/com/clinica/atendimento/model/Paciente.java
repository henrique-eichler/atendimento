package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "paciente")
public record Paciente(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    Pessoa pessoa,

    @OneToMany(mappedBy = "paciente")
    Set<ConvenioPaciente> convenios,

    @OneToMany(mappedBy = "paciente")
    Set<ResponsavelPaciente> responsaveis
) {
    // Custom constructor to initialize collections
    public Paciente {
        convenios = convenios != null ? Collections.unmodifiableSet(convenios) : Collections.emptySet();
        responsaveis = responsaveis != null ? Collections.unmodifiableSet(responsaveis) : Collections.emptySet();
    }

    // Constructor for JPA
    public Paciente(Long id, Pessoa pessoa) {
        this(id, pessoa, new LinkedHashSet<>(), new LinkedHashSet<>());
    }
}
