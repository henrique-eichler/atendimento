package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Cronograma;
import br.com.estimular.atendimento.model.enums.DiaSemana;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class CronogramaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private DiaSemana diaSemana;
    @JsonProperty
    private Instant horaInicio;
    @JsonProperty
    private Instant horaTermino;

    // Static method to convert from entity to DTO
    public static CronogramaDTO fromEntity(Cronograma cronograma) {
        if (cronograma == null) {
            return null;
        }

        return CronogramaDTO.builder()
                .id(cronograma.id())
                .diaSemana(cronograma.diaSemana())
                .horaInicio(cronograma.horaInicio())
                .horaTermino(cronograma.horaTermino())
                .build();
    }

    // Method to convert from DTO to entity
    public Cronograma toEntity() {
        return Cronograma.builder()
                .id(id)
                .diaSemana(diaSemana)
                .horaInicio(horaInicio)
                .horaTermino(horaTermino)
                .build();
    }
}
