package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.Profissional;

public record ProfissionalDTO(
    Long id,
    PessoaDTO pessoa
) {
    // Static method to convert from entity to DTO
    public static ProfissionalDTO fromEntity(Profissional profissional) {
        if (profissional == null) {
            return null;
        }
        
        return new ProfissionalDTO(
            profissional.id(),
            PessoaDTO.fromEntity(profissional.pessoa())
        );
    }

    // Method to convert from DTO to entity
    public Profissional toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;
        return new Profissional(
            id,
            pessoaEntity
        );
    }
}