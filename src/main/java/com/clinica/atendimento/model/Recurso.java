package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.TipoRecursoConverter;
import com.clinica.atendimento.model.enums.TipoRecurso;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "recurso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_recurso", columnNames = {"descricao", "tipo"})
})
public record Recurso(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "descricao", nullable = false, length = 100)
    String descricao,

    @Column(name = "tipo", nullable = false, length = 1)
    @Convert(converter = TipoRecursoConverter.class)
    TipoRecurso tipo,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurso_pai")
    Recurso recursoPai,

    @OneToMany(mappedBy = "recursoPai")
    Set<Recurso> filhos
) {
    // Custom constructor to initialize collections
    public Recurso {
        filhos = filhos != null ? Collections.unmodifiableSet(filhos) : Collections.emptySet();
    }

    // Constructor for JPA
    public Recurso(Long id, String descricao, TipoRecurso tipo, Recurso recursoPai) {
        this(id, descricao, tipo, recursoPai, new LinkedHashSet<>());
    }

    // Constructor for backward compatibility
    public Recurso(Long id, String descricao, String tipoCodigo, Recurso recursoPai) {
        this(id, descricao, tipoCodigo != null ? TipoRecurso.fromCodigo(tipoCodigo) : null, recursoPai, new LinkedHashSet<>());
    }
}
