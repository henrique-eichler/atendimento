package br.com.estimular.atendimento.dto;

import br.com.estimular.atendimento.model.Recurso;
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
public class RecursoDTO {

    @JsonProperty
    private Long id;
    @JsonProperty
    private String nome;
    @JsonProperty
    private String descricao;
    @JsonProperty
    private Integer numeroPropriedade;

    // Static method to convert from entity to DTO
    public static RecursoDTO fromEntity(Recurso recurso) {
        if (recurso == null) {
            return null;
        }

        return RecursoDTO.builder()
                .id(recurso.id())
                .nome(recurso.nome())
                .descricao(recurso.descricao())
                .numeroPropriedade(recurso.numeroPropriedade())
                .build();
    }

    // Method to convert from DTO to entity
    public Recurso toEntity() {
        Recurso recurso = new Recurso();
        recurso.id(id);
        recurso.nome(nome);
        recurso.descricao(descricao);
        recurso.numeroPropriedade(numeroPropriedade);
        return recurso;
    }
}
