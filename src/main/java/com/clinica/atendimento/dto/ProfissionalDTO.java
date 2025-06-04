package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.Profissional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class ProfissionalDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private PessoaDTO pessoa;

    // Static method to convert from entity to DTO
    public static ProfissionalDTO fromEntity(Profissional profissional) {
        if (profissional == null) {
            return null;
        }

        return ProfissionalDTO.builder()
                .id(profissional.pessoa().id())
                .pessoa(PessoaDTO.fromEntity(profissional.pessoa()))
                .build();
    }

    // Method to convert from DTO to entity
    public Profissional toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id);
        }

        return Profissional.builder()
                .pessoa(pessoaEntity)
                .build();
    }
}
