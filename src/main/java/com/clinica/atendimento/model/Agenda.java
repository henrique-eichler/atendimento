
package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Agenda {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Paciente paciente;

    @ManyToOne
    private Responsavel responsavel;

    @ManyToOne
    private Profissional profissional;

    private LocalDateTime dataHora;

    @OneToOne(cascade = CascadeType.ALL)
    private Sessao sessao;

}
