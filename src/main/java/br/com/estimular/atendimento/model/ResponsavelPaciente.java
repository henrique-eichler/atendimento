package br.com.estimular.atendimento.model;

import br.com.estimular.atendimento.model.converters.GrauParentescoConverter;
import br.com.estimular.atendimento.model.enums.GrauParentesco;
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
@Table(name = "responsavel_paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_responsavel_paciente", columnNames = {"responsavel", "paciente"})
})
public class ResponsavelPaciente {
    @Id
    @SequenceGenerator(name = "responsavel_paciente_seq", sequenceName = "responsavel_paciente_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "responsavel_paciente_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel", nullable = false)
    private Responsavel responsavel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    private Paciente paciente;

    @Column(name = "grau_parentesco", nullable = false, length = 1)
    @Convert(converter = GrauParentescoConverter.class)
    private GrauParentesco grauParentesco;
}
