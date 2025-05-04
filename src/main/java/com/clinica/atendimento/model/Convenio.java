package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "convenio", uniqueConstraints = {
        @UniqueConstraint(name = "uk_covenio_nome", columnNames = {"nome"})
})
public record Convenio(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "nome", nullable = false)
    String nome,

    @OneToMany(mappedBy = "convenio")
    Set<TerapiaConvenio> terapias
) {
    // Custom constructor to initialize collections
    public Convenio {
        terapias = terapias != null ? Collections.unmodifiableSet(terapias) : Collections.emptySet();
    }

    // Constructor for JPA
    public Convenio(Long id, String nome) {
        this(id, nome, new LinkedHashSet<>());
    }
}
