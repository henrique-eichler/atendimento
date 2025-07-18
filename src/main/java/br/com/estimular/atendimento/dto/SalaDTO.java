package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Sala;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class SalaDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private Long numero;
    @JsonProperty
    private List<TerapiaDTO> terapias;
    @JsonProperty
    private List<RecursoDTO> recursos;
    @JsonProperty
    private List<CronogramaDTO> cronogramas;

    // Static method to convert from entity to DTO
    public static SalaDTO fromEntity(Sala sala) {
        if (sala == null) {
            return null;
        }

        List<TerapiaDTO> terapiaDTOs = sala.terapias().stream()
                .map(terapiaSala -> TerapiaDTO.fromEntity(terapiaSala.terapia()))
                .toList();

        List<RecursoDTO> recursoDTOs = sala.recursos().stream()
                .map(recursoSala -> RecursoDTO.fromEntity(recursoSala.recurso()))
                .toList();

        List<CronogramaDTO> cronogramaDTOS = sala.cronogramas().stream()
                .map(CronogramaDTO::fromEntity)
                .toList();

        return SalaDTO.builder()
                .id(sala.id())
                .numero(sala.numero())
                .terapias(terapiaDTOs)
                .recursos(recursoDTOs)
                .cronogramas(cronogramaDTOS)
                .build();
    }

    // Method to convert from DTO to entity
    public Sala toEntity() {
        Sala.SalaBuilder builder = Sala.builder();

        // Only set ID if it's not null
        if (id != null) {
            builder.id(id);
        }

        return builder
                .numero(numero)
                .build();
    }
}
