package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.DiaSemanaConverter;
import com.clinica.atendimento.model.enums.DiaSemana;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "cronograma", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cronograma", columnNames = {"sala", "dia_semana", "hora_inicio"})
})
public record Cronograma(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    Sala sala,

    @Column(name = "dia_semana", nullable = false, length = 1)
    @Convert(converter = DiaSemanaConverter.class)
    DiaSemana diaSemana,

    @Column(name = "hora_inicio", nullable = false)
    Instant horaInicio,

    @Column(name = "hora_termino", nullable = false)
    Instant horaTermino
) {
}
