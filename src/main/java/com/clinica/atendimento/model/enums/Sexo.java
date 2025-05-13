package com.clinica.atendimento.model.enums;

import lombok.Getter;

@Getter
public enum Sexo {
    FEMININO("F"),
    MASCULINO("M");

    private final String codigo;

    Sexo(String codigo) {
        this.codigo = codigo;
    }

    public static Sexo fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (Sexo sexo : Sexo.values()) {
            if (sexo.getCodigo().equals(codigo)) {
                return sexo;
            }
        }
        throw new IllegalArgumentException("Código de sexo inválido: " + codigo);
    }
}