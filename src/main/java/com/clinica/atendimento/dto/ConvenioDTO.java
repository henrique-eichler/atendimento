package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Convenio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConvenioDTO {
    private Long id;
    private String nome;

    // Static method to convert from entity to DTO
    public static ConvenioDTO fromEntity(Convenio convenio) {
        if (convenio == null) {
            return null;
        }

        return ConvenioDTO.builder()
                .id(convenio.id())
                .nome(convenio.nome())
                .build();
    }

    // Method to convert from DTO to entity
    public Convenio toEntity() {
        // Using the constructor for backward compatibility
        // return new Convenio(id, nome);

        // Using the builder pattern
        Convenio.ConvenioBuilder builder = Convenio.builder();

        // Only set ID if it's not null
        if (id != null) {
            builder.id(id);
        }

        return builder
                .nome(nome)
                .build();
    }
}
