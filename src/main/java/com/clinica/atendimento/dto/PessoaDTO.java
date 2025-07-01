package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class PessoaDTO {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE;

    @JsonProperty private Long id;
    @JsonProperty private String nome;
    @JsonProperty private String email;
    @JsonProperty private String dataNascimento;
    @JsonProperty private String sexo;

    // Static method to convert from entity to DTO
    public static PessoaDTO fromEntity(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return PessoaDTO.builder()
                .id(pessoa.id())
                .nome(pessoa.nome())
                .email(pessoa.email())
                .dataNascimento(pessoa.dataNascimento().format(DATE_TIME_FORMATTER))
                .sexo(pessoa.sexo().getCodigo())
                .build();
    }

    // Method to convert from DTO to entity
    public Pessoa toEntity() {
        return Pessoa.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .dataNascimento(LocalDate.from(DATE_TIME_FORMATTER.parse(dataNascimento)))
                .sexo(Sexo.fromCodigo(sexo))
                .build();
    }
}