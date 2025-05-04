package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "profissional_terapia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_profissional_terapia", columnNames = {"profissional", "terapia", "data_validade"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        ProfissionalTerapia that = (ProfissionalTerapia) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
