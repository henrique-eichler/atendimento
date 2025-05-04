package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "gestao", uniqueConstraints = {
        @UniqueConstraint(name = "uk_gestao", columnNames = {"descricao", "gestao_pai"})
})
public record Gestao(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "descricao", nullable = false)
    String descricao,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gestao_pai")
    Gestao gestaoPai,

    @OneToMany(mappedBy = "gestaoPai")
    Set<Gestao> filhos,

    @OneToMany(mappedBy = "gestao")
    Set<GestaoDocumento> documentos
) {
    // Custom constructor to initialize collections
    public Gestao {
        filhos = filhos != null ? Collections.unmodifiableSet(filhos) : Collections.emptySet();
        documentos = documentos != null ? Collections.unmodifiableSet(documentos) : Collections.emptySet();
    }

    // Constructor for JPA
    public Gestao(Long id, String descricao, Gestao gestaoPai) {
        this(id, descricao, gestaoPai, new LinkedHashSet<>(), new LinkedHashSet<>());
    }
}
