package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.Usuario;

public record UsuarioDTO(
    Long id,
    PessoaDTO pessoa,
    String senha
) {
    // Static method to convert from entity to DTO
    public static UsuarioDTO fromEntity(Usuario usuario) {
        if (usuario == null) {
            return null;
        }
        
        return new UsuarioDTO(
            usuario.id(),
            PessoaDTO.fromEntity(usuario.pessoa()),
            usuario.senha()
        );
    }

    // Method to convert from DTO to entity
    public Usuario toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;
        return new Usuario(
            id,
            pessoaEntity,
            senha
        );
    }
}