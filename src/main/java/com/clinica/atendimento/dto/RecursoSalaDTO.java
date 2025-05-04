package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;

public record RecursoSalaDTO(
        Long id,
        RecursoDTO recurso,
        SalaDTO sala
) {
    // Static method to convert from entity to DTO
    public static RecursoSalaDTO fromEntity(RecursoSala recursoSala) {
        if (recursoSala == null) {
            return null;
        }

        return new RecursoSalaDTO(
                recursoSala.getId(),
                RecursoDTO.fromEntity(recursoSala.getRecurso()),
                SalaDTO.fromEntity(recursoSala.getSala())
        );
    }

    // Method to convert from DTO to entity
    public RecursoSala toEntity() {
        RecursoSala recursoSala = new RecursoSala();
        recursoSala.setId(id);

        if (recurso != null) {
            recursoSala.setRecurso(recurso.toEntity());
        }

        if (sala != null) {
            Sala salaEntity = sala.toEntity();
            recursoSala.setSala(salaEntity);
        }

        return recursoSala;
    }
}