package com.clinica.atendimento.model.enums;

import lombok.Getter;

@Getter
public enum DiaSemana {
    DOMINGO("D"),
    SEGUNDA("2"),
    TERCA("3"),
    QUARTA("4"),
    QUINTA("5"),
    SEXTA("6"),
    SABADO("S");

    private final String codigo;

    DiaSemana(String codigo) {
        this.codigo = codigo;
    }

    public static DiaSemana fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (DiaSemana diaSemana : DiaSemana.values()) {
            if (diaSemana.getCodigo().equals(codigo)) {
                return diaSemana;
            }
        }
        throw new IllegalArgumentException("Código de dia da semana inválido: " + codigo);
    }
}