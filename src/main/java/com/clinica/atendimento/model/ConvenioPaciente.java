package com.clinica.atendimento.model;

import jakarta.persistence.*;

@Entity
@Table(name = "convenio_paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_convenio_paciente_paciente", columnNames = {"convenio", "paciente"}),
        @UniqueConstraint(name = "uk_convenio_paciente_numero", columnNames = {"convenio", "numero"})
})
public record ConvenioPaciente(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convenio", nullable = false)
    Convenio convenio,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "paciente", nullable = false)
    Paciente paciente,

    @Column(name = "numero", nullable = false, length = 100)
    String numero
) {
}
