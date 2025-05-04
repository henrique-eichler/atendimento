package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.DiaSemanaConverter;
import com.clinica.atendimento.model.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "cronograma", uniqueConstraints = {
        @UniqueConstraint(name = "uk_cronograma", columnNames = {"sala", "dia_semana", "hora_inicio"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class Cronograma {
    @Id
    @SequenceGenerator(name = "cronograma_seq", sequenceName = "cronograma_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cronograma_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sala", nullable = false)
    private Sala sala;

    @Column(name = "dia_semana", nullable = false, length = 1)
    @Convert(converter = DiaSemanaConverter.class)
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private Instant horaInicio;

    @Column(name = "hora_termino", nullable = false)
    private Instant horaTermino;

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Cronograma that = (Cronograma) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
