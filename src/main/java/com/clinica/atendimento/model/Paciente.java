
package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@Entity
public class Paciente {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private LocalDate dataNascimento;
    private Sexo sexo;

    @ManyToMany
    @JoinTable(name = "paciente_responsavel",
        joinColumns = @JoinColumn(name = "paciente_id"),
        inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private Set<Responsavel> responsaveis;

    public static enum Sexo {
        MASCULINO, FEMININO;
    }
}
