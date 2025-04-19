
package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Audio {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String caminho;
    private Long duracaoSegundos;

    @ManyToOne
    private Sessao sessao;

}
