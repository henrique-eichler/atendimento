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
                .id(recurso.id())
                .nome(recurso.nome())
                .descricao(recurso.descricao())
                .numeroPropriedade(recurso.numeroPropriedade())
                .build();
    }

    // Method to convert from DTO to entity
    public Recurso toEntity() {
        Recurso recurso = new Recurso();
        recurso.id(id);
        recurso.nome(nome);
        recurso.descricao(descricao);
        recurso.numeroPropriedade(numeroPropriedade);
        return recurso;
    }
}
