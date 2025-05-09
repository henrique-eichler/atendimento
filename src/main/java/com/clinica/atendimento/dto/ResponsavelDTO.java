package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.Responsavel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsavelDTO {
    private Long id;
    private PessoaDTO pessoa;

    // Static method to convert from entity to DTO
    public static ResponsavelDTO fromEntity(Responsavel responsavel) {
        if (responsavel == null) {
            return null;
        }

        return ResponsavelDTO.builder()
            .id(responsavel.pessoa().id())
            .pessoa(PessoaDTO.fromEntity(responsavel.pessoa()))
            .build();
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
