package com.clinica.atendimento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "gestao_documento")
public record GestaoDocumento(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gestao", nullable = false)
    Gestao gestao,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "documento", nullable = false)
    Documento documento
) {
}
