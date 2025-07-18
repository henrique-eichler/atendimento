package br.com.estimular.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "recurso", uniqueConstraints = {
        @UniqueConstraint(name = "uk_recurso_descricao", columnNames = {"nome"}),
        @UniqueConstraint(name = "uk_resuro_numero_propriedade", columnNames = {"numero_propriedade"})
})
public class Recurso {
    @Id
    @SequenceGenerator(name = "recurso_seq", sequenceName = "recurso_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recurso_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @Column(name = "numero_propriedade", nullable = false)
    private Integer numeroPropriedade;

    @OneToMany(mappedBy = "recurso")
    private Set<RecursoSala> salas = new LinkedHashSet<>();
}