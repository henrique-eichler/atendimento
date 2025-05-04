package com.clinica.atendimento.model;

import com.clinica.atendimento.model.converters.TipoAcessoConverter;
import com.clinica.atendimento.model.enums.TipoAcesso;
import jakarta.persistence.*;

@Entity
@Table(name = "grupo_usuario", uniqueConstraints = {
        @UniqueConstraint(name = "uk_grupo_usuario", columnNames = {"grupo", "usuario"})
})
public record GrupoUsuario(
    @Id
    @Column(name = "id", nullable = false)
    Long id,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo", nullable = false)
    Grupo grupo,

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario", nullable = false)
    Usuario usuario,

    @Column(name = "tipo", nullable = false, length = 1)
    @Convert(converter = TipoAcessoConverter.class)
    TipoAcesso tipo
) {
}
