package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.GrauParentescoConverter;
import com.clinica.atendimento.model.enums.GrauParentesco;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.util.Objects;

@Entity
@Table(name = "responsavel_paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_responsavel_paciente", columnNames = {"responsavel", "paciente"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ResponsavelPaciente that = (ResponsavelPaciente) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
