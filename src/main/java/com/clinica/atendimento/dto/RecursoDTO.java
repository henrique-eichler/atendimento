package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Recurso;

public record RecursoDTO(
    Long id,
    String nome,
    String descricao,
    Integer numeroPropriedade
) {
    // Static method to convert from entity to DTO
    public static RecursoDTO fromEntity(Recurso recurso) {
        if (recurso == null) {
            return null;
        }

        return new RecursoDTO(
            recurso.getId(),
            recurso.getNome(),
            recurso.getDescricao(),
            recurso.getNumeroPropriedade()
        );
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