package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoSalaDTO {
    private Long id;
    private RecursoDTO recurso;
    private SalaDTO sala;

    // Static method to convert from entity to DTO
    public static RecursoSalaDTO fromEntity(RecursoSala recursoSala) {
        if (recursoSala == null) {
            return null;
        }

        return RecursoSalaDTO.builder()
                .id(recursoSala.getId())
                .recurso(RecursoDTO.fromEntity(recursoSala.getRecurso()))
                .sala(SalaDTO.fromEntity(recursoSala.getSala()))
                .build();
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
