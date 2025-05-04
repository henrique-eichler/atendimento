package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "sessao")
public record Sessao(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id", nullable = false)
    Agenda agenda,

    @Column(name = "data_inicio", nullable = false)
    Instant dataInicio,

    @Column(name = "data_termino")
    Instant dataTermino,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    Sala sala,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    Paciente paciente
) {
}
