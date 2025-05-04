package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Grupo;
import com.clinica.atendimento.model.GrupoUsuario;
import com.clinica.atendimento.model.Usuario;
import com.clinica.atendimento.model.enums.TipoAcesso;

public record GrupoUsuarioDTO(
        Long id,
        GrupoDTO grupo,
        UsuarioDTO usuario,
        TipoAcesso tipo
) {
    // Static method to convert from entity to DTO
    public static GrupoUsuarioDTO fromEntity(GrupoUsuario grupoUsuario) {
        if (grupoUsuario == null) {
            return null;
        }
        
        return new GrupoUsuarioDTO(
            grupoUsuario.id(),
            GrupoDTO.fromEntity(grupoUsuario.grupo()),
            UsuarioDTO.fromEntity(grupoUsuario.usuario()),
            grupoUsuario.tipo()
        );
    }

    // Method to convert from DTO to entity
    public GrupoUsuario toEntity() {
        Grupo grupoEntity = grupo != null ? grupo.toEntity() : null;
        Usuario usuarioEntity = usuario != null ? usuario.toEntity() : null;
        
        return new GrupoUsuario(
            id,
            grupoEntity,
            usuarioEntity,
            tipo
        );
    }
}