package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "recurso_sala")
public class RecursoSala {
    @Id
    @SequenceGenerator(name = "recurso_sala_seq", sequenceName = "recurso_sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurso_sala_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recurso", nullable = false)
    private Recurso recurso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    private Sala sala;

}
