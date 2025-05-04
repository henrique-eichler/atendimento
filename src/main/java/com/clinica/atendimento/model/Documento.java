package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "documento")
public record Documento(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "nome", nullable = false)
    String nome,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_documento", nullable = false)
    TipoDocumento tipoDocumento,

    @Column(name = "conteudo", nullable = false)
    byte[] conteudo,

    @OneToMany(mappedBy = "documento")
    Set<GestaoDocumento> gestoes
) {
    // Custom constructor to initialize collections
    public Documento {
        gestoes = gestoes != null ? Collections.unmodifiableSet(gestoes) : Collections.emptySet();
    }

    // Constructor for JPA
    public Documento(Long id, String nome, TipoDocumento tipoDocumento, byte[] conteudo) {
        this(id, nome, tipoDocumento, conteudo, new LinkedHashSet<>());
    }
}
