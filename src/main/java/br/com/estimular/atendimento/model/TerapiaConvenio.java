package br.com.estimular.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "terapia_convenio", uniqueConstraints = {@UniqueConstraint(name = "uk_terapia_convenio", columnNames = {"terapia", "convenio"})})
public class TerapiaConvenio {
    @Id
    @SequenceGenerator(name = "terapia_convenio_seq", sequenceName = "terapia_convenio_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_convenio_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    private Terapia terapia;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    private Convenio convenio;

    @Column(name = "valor_terapia", nullable = false)
    private Float valorTerapia;

    @Column(name = "valor_profissional", nullable = false)
    private Float valorProfissional;
}
