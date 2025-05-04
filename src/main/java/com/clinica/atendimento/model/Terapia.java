package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "terapia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_terapia_nome", columnNames = {"nome"})
})
public record Terapia(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "nome", nullable = false)
    String nome,

    @OneToMany(mappedBy = "terapia")
    Set<ProfissionalTerapia> profissionais,

    @OneToMany(mappedBy = "terapia")
    Set<TerapiaConvenio> convenios,

    @OneToMany(mappedBy = "terapia")
    Set<TerapiaSala> salas
) {
    // Custom constructor to initialize collections
    public Terapia {
        profissionais = profissionais != null ? Collections.unmodifiableSet(profissionais) : Collections.emptySet();
        convenios = convenios != null ? Collections.unmodifiableSet(convenios) : Collections.emptySet();
        salas = salas != null ? Collections.unmodifiableSet(salas) : Collections.emptySet();
    }

    // Constructor for JPA
    public Terapia(Long id, String nome) {
        this(id, nome, new LinkedHashSet<>(), new LinkedHashSet<>(), new LinkedHashSet<>());
    }
}
