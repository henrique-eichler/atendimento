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
@Table(name = "convenio_paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_convenio_paciente_paciente", columnNames = {"convenio", "paciente"}),
        @UniqueConstraint(name = "uk_convenio_paciente_numero", columnNames = {"convenio", "numero"})
})
public class ConvenioPaciente {
    @Id
    @SequenceGenerator(name = "convenio_paciente_seq", sequenceName = "convenio_paciente_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "convenio_paciente_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    private Convenio convenio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    private Paciente paciente;

    @Column(name = "numero", nullable = false, length = 100)
    private String numero;
}
