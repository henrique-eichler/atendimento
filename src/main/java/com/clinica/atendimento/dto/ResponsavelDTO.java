package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.Responsavel;

public record ResponsavelDTO(
    Long id,
    PessoaDTO pessoa
) {
    // Static method to convert from entity to DTO
    public static ResponsavelDTO fromEntity(Responsavel responsavel) {
        if (responsavel == null) {
            return null;
        }
        
        return new ResponsavelDTO(
            responsavel.id(),
            PessoaDTO.fromEntity(responsavel.pessoa())
        );
    }

    // Method to convert from DTO to entity
    public Responsavel toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;
        return new Responsavel(
            id,
            pessoaEntity
        );
    }
}