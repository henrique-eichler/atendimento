
package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Sessao {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String textoOriginal;

    @Column(columnDefinition = "TEXT")
    private String transcricao;

    private String interpretacao;
    private LocalDateTime registradaEm;

    @OneToMany(mappedBy = "sessao", cascade = CascadeType.ALL)
    private List<Audio> audios;

}
