package com.clinica.atendimento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "terapia_convenio", uniqueConstraints = {
        @UniqueConstraint(name = "uk_terapia_convenio", columnNames = {"terapia", "convenio"})
})
public record TerapiaConvenio(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    Terapia terapia,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    Convenio convenio,

    @Column(name = "valor_terapia", nullable = false)
    Float valorTerapia,

    @Column(name = "valor_profissional", nullable = false)
    Float valorProfissional
) {
}
