package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Recurso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDTO {
    private Long id;
    private String nome;
    private String descricao;
    private Integer numeroPropriedade;

    // Static method to convert from entity to DTO
    public static RecursoDTO fromEntity(Recurso recurso) {
        if (recurso == null) {
            return null;
        }

        return RecursoDTO.builder()
            .id(recurso.getId())
            .nome(recurso.getNome())
            .descricao(recurso.getDescricao())
            .numeroPropriedade(recurso.getNumeroPropriedade())
            .build();
    }

    // Method to convert from DTO to entity
    public Recurso toEntity() {
        Recurso recurso = new Recurso();
        recurso.setId(id);
        recurso.setNome(nome);
        recurso.setDescricao(descricao);
        recurso.setNumeroPropriedade(numeroPropriedade);
        return recurso;
    }
}
