package com.clinica.atendimento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "terapia_sala", uniqueConstraints = {
        @UniqueConstraint(name = "uk_terapia_sala", columnNames = {"terapia", "sala"})
})
public record TerapiaSala(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    Terapia terapia,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    Sala sala
) {
}
