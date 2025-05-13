package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.enums.Sexo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PessoaDTO {
    private Long id;
    private String nome;
    private String email;
    private LocalDate dataNascimento;
    private Sexo sexo;

    // Static method to convert from entity to DTO
    public static PessoaDTO fromEntity(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return PessoaDTO.builder()
                .id(pessoa.id())
                .nome(pessoa.nome())
                .email(pessoa.email())
                .dataNascimento(pessoa.dataNascimento())
                .sexo(pessoa.sexo())
                .build();
    }

    // Method to convert from DTO to entity
    public Pessoa toEntity() {
        return Pessoa.builder()
                .id(id)
                .nome(nome)
                .email(email)
                .dataNascimento(dataNascimento)
                .sexo(sexo)
                .build();
    }
}
