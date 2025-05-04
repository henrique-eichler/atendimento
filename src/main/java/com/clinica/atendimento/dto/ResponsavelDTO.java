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
            responsavel.pessoa().id(),
            PessoaDTO.fromEntity(responsavel.pessoa())
        );
    }

    // Method to convert from DTO to entity
    public Responsavel toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id);
        }

        return Responsavel.builder()
            .pessoa(pessoaEntity)
            .build();
    }
}
