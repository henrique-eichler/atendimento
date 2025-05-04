package com.clinica.atendimento.model.enums;

import lombok.Getter;

@Getter
public enum TipoRecurso {
    MENU("M"),
    ACAO("A"),
    OUTRO("O");

    private final String codigo;

    TipoRecurso(String codigo) {
        this.codigo = codigo;
    }

    public static TipoRecurso fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        
        for (TipoRecurso tipoRecurso : TipoRecurso.values()) {
            if (tipoRecurso.getCodigo().equals(codigo)) {
                return tipoRecurso;
            }
        }
        throw new IllegalArgumentException("Código de tipo de recurso inválido: " + codigo);
    }
}