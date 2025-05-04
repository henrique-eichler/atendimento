package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "profissional_terapia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_profissional_terapia", columnNames = {"profissional", "terapia", "data_validade"})
})
public record ProfissionalTerapia(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional", nullable = false)
    Profissional profissional,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    Terapia terapia,

    @Column(name = "data_validade", nullable = false)
    LocalDate dataValidade
) {
}
