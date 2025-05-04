package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "usuario")
public record Usuario(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    Pessoa pessoa,

    @Column(name = "senha", nullable = false)
    String senha,

    @OneToMany(mappedBy = "usuario")
    Set<GrupoUsuario> grupos
) {
    // Custom constructor to initialize collections
    public Usuario {
        grupos = grupos != null ? Collections.unmodifiableSet(grupos) : Collections.emptySet();
    }

    // Constructor for JPA
    public Usuario(Long id, Pessoa pessoa, String senha) {
        this(id, pessoa, senha, new LinkedHashSet<>());
    }
}
