package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Convenio;

public record ConvenioDTO(
    Long id,
    String nome
) {
    // Static method to convert from entity to DTO
    public static ConvenioDTO fromEntity(Convenio convenio) {
        if (convenio == null) {
            return null;
        }
        
        return new ConvenioDTO(
            convenio.id(),
            convenio.nome()
        );
    }

    // Method to convert from DTO to entity
    public Convenio toEntity() {
        return new Convenio(
            id,
            nome
        );
    }
}