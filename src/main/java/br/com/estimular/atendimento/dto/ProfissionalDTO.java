package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Pessoa;
import br.com.estimular.atendimento.model.Profissional;
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
public class ProfissionalDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private PessoaDTO pessoa;
    @JsonProperty
    private List<TerapiaDTO> terapias;

    // Static method to convert from entity to DTO
    public static ProfissionalDTO fromEntity(Profissional profissional) {
        if (profissional == null) {
            return null;
        }

        List<TerapiaDTO> terapiaDTOs = profissional.terapias().stream()
                .map(profissionalTerapia -> TerapiaDTO.fromEntity(profissionalTerapia.terapia()))
                .toList();

        return ProfissionalDTO.builder()
                .id(profissional.pessoa().id())
                .pessoa(PessoaDTO.fromEntity(profissional.pessoa()))
                .terapias(terapiaDTOs)
                .build();
    }

    // Method to convert from DTO to entity
    public Profissional toEntity() {
        Pessoa pessoaEntity = pessoa != null ? pessoa.toEntity() : null;

        // Set the ID on the Pessoa entity
        if (pessoaEntity != null && id != null) {
            pessoaEntity.id(id);
        }

        return Profissional.builder()
                .pessoa(pessoaEntity)
                .build();
    }
}
