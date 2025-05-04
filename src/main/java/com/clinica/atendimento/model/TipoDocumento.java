package com.clinica.atendimento.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tipo_documento")
public record TipoDocumento(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "descricao", nullable = false)
    String descricao,

    @Column(name = "mimetype", nullable = false)
    String mimetype
) {
}
