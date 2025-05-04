package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.GrauParentescoConverter;
import com.clinica.atendimento.model.enums.GrauParentesco;
import jakarta.persistence.*;

@Entity
@Table(name = "responsavel_paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_responsavel_paciente", columnNames = {"responsavel", "paciente"})
})
public record ResponsavelPaciente(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "responsavel", nullable = false)
    Responsavel responsavel,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    Paciente paciente,

    @Column(name = "grau_parentesco", nullable = false, length = 1)
    @Convert(converter = GrauParentescoConverter.class)
    GrauParentesco grauParentesco
) {
}
