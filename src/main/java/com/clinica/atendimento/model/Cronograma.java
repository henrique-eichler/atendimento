package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.DiaSemanaConverter;
import com.clinica.atendimento.model.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.Instant;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "cronograma", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cronograma", columnNames = {"sala", "dia_semana", "hora_inicio"})
})
public class Cronograma {
    @Id
    @SequenceGenerator(name = "cronograma_seq", sequenceName = "cronograma_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cronograma_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    private Sala sala;

    @Column(name = "dia_semana", nullable = false, length = 1)
    @Convert(converter = DiaSemanaConverter.class)
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private Instant horaInicio;

    @Column(name = "hora_termino", nullable = false)
    private Instant horaTermino;
}
