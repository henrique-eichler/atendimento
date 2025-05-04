package com.clinica.atendimento.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "terapia", uniqueConstraints = {
        @UniqueConstraint(name = "uk_terapia_nome", columnNames = {"nome"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(fluent = true)
public class Terapia {
    @Id
    @SequenceGenerator(name = "terapia_seq", sequenceName = "terapia_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "terapia_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "nome", nullable = false)
    private String nome;

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<ProfissionalTerapia> profissionais = new LinkedHashSet<>();

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<TerapiaConvenio> convenios = new LinkedHashSet<>();

    @OneToMany(mappedBy = "terapia")
    @Builder.Default
    private Set<TerapiaSala> salas = new LinkedHashSet<>();

    // Constructor for backward compatibility with DTOs
    public Terapia(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Terapia terapia = (Terapia) o;
        return id != null && Objects.equals(id, terapia.id);
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
