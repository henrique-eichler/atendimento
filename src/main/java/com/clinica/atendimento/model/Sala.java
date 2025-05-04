package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "sala", uniqueConstraints = {
        @UniqueConstraint(name = "uk_sala_numvero", columnNames = {"numero"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class Sala {
    @Id
    @SequenceGenerator(name = "sala_seq", sequenceName = "sala_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sala_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "numero", nullable = false)
    private Long numero;

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<Cronograma> cronogramas = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<TerapiaSala> terapias = new LinkedHashSet<>();

    @OneToMany(mappedBy = "sala")
    @Builder.Default
    private Set<RecursoSala> recursos = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Sala sala = (Sala) o;
        return id != null && Objects.equals(id, sala.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
