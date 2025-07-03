package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Terapia;
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
public class TerapiaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private String nome;

    // Static method to convert from entity to DTO
    public static TerapiaDTO fromEntity(Terapia terapia) {
        if (terapia == null) {
            return null;
        }

        return TerapiaDTO.builder()
                .id(terapia.id())
                .nome(terapia.nome())
                .build();
    }

    // Method to convert from DTO to entity
    public Terapia toEntity() {
        // Using the constructor for backward compatibility
        // return new Terapia(id, nome);

        // Using the builder pattern
        Terapia.TerapiaBuilder builder = Terapia.builder();

        // Only set ID if it's not null
        if (id != null) {
            builder.id(id);
        }

        return builder
                .nome(nome)
                .build();
    }
}
