package com.clinica.atendimento.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "agenda")
public record Agenda(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @Column(name = "data_agenda", nullable = false)
    LocalDate dataAgenda,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cronograma", nullable = false)
    Cronograma cronograma,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    Paciente paciente,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    Terapia terapia,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    Convenio convenio,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional", nullable = false)
    Profissional profissional,

    @OneToOne(mappedBy = "agenda")
    Sessao sessao
) {
    // Constructor for JPA
    public Agenda(Long id, LocalDate dataAgenda, Cronograma cronograma, Paciente paciente, 
                 Terapia terapia, Convenio convenio, Profissional profissional) {
        this(id, dataAgenda, cronograma, paciente, terapia, convenio, profissional, null);
    }
}
