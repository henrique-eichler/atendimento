package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Sala;
import br.com.estimular.atendimento.model.Terapia;
import br.com.estimular.atendimento.model.TerapiaSala;
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
public class TerapiaSalaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private TerapiaDTO terapia;
    @JsonProperty
    private SalaDTO sala;

    // Static method to convert from entity to DTO
    public static TerapiaSalaDTO fromEntity(TerapiaSala terapiaSala) {
        if (terapiaSala == null) {
            return null;
        }

        return TerapiaSalaDTO.builder()
                .id(terapiaSala.id())
                .terapia(TerapiaDTO.fromEntity(terapiaSala.terapia()))
                .sala(SalaDTO.fromEntity(terapiaSala.sala()))
                .build();
    }

    // Method to convert from DTO to entity
    public TerapiaSala toEntity() {
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;
        Sala salaEntity = sala != null ? sala.toEntity() : null;

        return TerapiaSala.builder()
                .id(id)
                .terapia(terapiaEntity)
                .sala(salaEntity)
                .build();
    }
}
