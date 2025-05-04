package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Pessoa;
import com.clinica.atendimento.model.enums.Sexo;

import java.time.LocalDate;

public record PessoaDTO(
        Long id,
        String nome,
        String email,
        LocalDate dataNascimento,
        Sexo sexo
) {
    // Static method to convert from entity to DTO
    public static PessoaDTO fromEntity(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }

        return new PessoaDTO(
            pessoa.id(),
            pessoa.nome(),
            pessoa.email(),
            pessoa.dataNascimento(),
            pessoa.sexo()
        );
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
