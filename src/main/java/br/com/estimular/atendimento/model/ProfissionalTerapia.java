package br.com.estimular.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@EqualsAndHashCode(of = {"id"})
@Table(name = "profissional_terapia", uniqueConstraints = {@UniqueConstraint(name = "uk_profissional_terapia", columnNames = {"profissional", "terapia", "data_validade"})})
public class ProfissionalTerapia {
    @Id
    @SequenceGenerator(name = "profissional_terapia_seq", sequenceName = "profissional_terapia_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "profissional_terapia_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "profissional", nullable = false)
    private Profissional profissional;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terapia", nullable = false)
    private Terapia terapia;

    @Column(name = "data_validade", nullable = false)
    private LocalDate dataValidade;
}
