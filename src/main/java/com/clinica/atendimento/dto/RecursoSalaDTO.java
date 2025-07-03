package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class RecursoSalaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private RecursoDTO recurso;
    @JsonProperty
    private SalaDTO sala;

    // Static method to convert from entity to DTO
    public static RecursoSalaDTO fromEntity(RecursoSala recursoSala) {
        if (recursoSala == null) {
            return null;
        }

        return RecursoSalaDTO.builder()
                .id(recursoSala.id())
                .recurso(RecursoDTO.fromEntity(recursoSala.recurso()))
                .sala(SalaDTO.fromEntity(recursoSala.sala()))
                .build();
    }

    // Method to convert from DTO to entity
    public RecursoSala toEntity() {
        RecursoSala recursoSala = new RecursoSala();
        recursoSala.id(id);

        if (recurso != null) {
            recursoSala.recurso(recurso.toEntity());
        }

        if (sala != null) {
            Sala salaEntity = sala.toEntity();
            recursoSala.sala(salaEntity);
        }

        return recursoSala;
    }
}
