package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "grupo", uniqueConstraints = {
        @UniqueConstraint(name = "uk_grupo_nome", columnNames = {"nome"})
})
public record Grupo(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "nome", nullable = false, length = 100)
    String nome,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_pai")
    Grupo grupoPai,

    @OneToMany(mappedBy = "grupoPai")
    Set<Grupo> filhos,

    @OneToMany(mappedBy = "grupo")
    Set<GrupoRecurso> recursos,

    @OneToMany(mappedBy = "grupo")
    Set<GrupoUsuario> usuarios
) {
    // Custom constructor to initialize collections
    public Grupo {
        filhos = filhos != null ? Collections.unmodifiableSet(filhos) : Collections.emptySet();
        recursos = recursos != null ? Collections.unmodifiableSet(recursos) : Collections.emptySet();
        usuarios = usuarios != null ? Collections.unmodifiableSet(usuarios) : Collections.emptySet();
    }

    // Constructor for JPA
    public Grupo(Long id, String nome, Grupo grupoPai) {
        this(id, nome, grupoPai, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
    }
}
