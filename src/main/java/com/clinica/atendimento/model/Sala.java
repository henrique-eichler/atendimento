package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "sala", uniqueConstraints = {
        @UniqueConstraint(name = "uk_sala_numvero", columnNames = {"numero"})
})
public record Sala(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "numero", nullable = false)
    Long numero,

    @OneToMany(mappedBy = "sala")
    Set<Cronograma> cronogramas,

    @OneToMany(mappedBy = "sala")
    Set<TerapiaSala> terapias
) {
    // Custom constructor to initialize collections
    public Sala {
        cronogramas = cronogramas != null ? Collections.unmodifiableSet(cronogramas) : Collections.emptySet();
        terapias = terapias != null ? Collections.unmodifiableSet(terapias) : Collections.emptySet();
    }

    // Constructor for JPA
    public Sala(Long id, Long numero) {
        this(id, numero, new LinkedHashSet<>(), new LinkedHashSet<>());
    }
}
