package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Profissional;
import br.com.estimular.atendimento.model.ProfissionalTerapia;
import br.com.estimular.atendimento.model.Terapia;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class ProfissionalTerapiaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private ProfissionalDTO profissional;
    @JsonProperty
    private TerapiaDTO terapia;
    @JsonProperty
    private LocalDate dataValidade;

    // Static method to convert from entity to DTO
    public static ProfissionalTerapiaDTO fromEntity(ProfissionalTerapia profissionalTerapia) {
        if (profissionalTerapia == null) {
            return null;
        }

        return ProfissionalTerapiaDTO.builder()
                .id(profissionalTerapia.id())
                .profissional(ProfissionalDTO.fromEntity(profissionalTerapia.profissional()))
                .terapia(TerapiaDTO.fromEntity(profissionalTerapia.terapia()))
                .dataValidade(profissionalTerapia.dataValidade())
                .build();
    }

    // Method to convert from DTO to entity
    public ProfissionalTerapia toEntity() {
        Profissional profissionalEntity = profissional != null ? profissional.toEntity() : null;
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;

        return ProfissionalTerapia.builder()
                .id(id)
                .profissional(profissionalEntity)
                .terapia(terapiaEntity)
                .dataValidade(dataValidade)
                .build();
    }
}
