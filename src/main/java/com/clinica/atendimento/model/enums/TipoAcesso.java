package com.clinica.atendimento.model.enums;

import lombok.Getter;

@Getter
public enum TipoAcesso {
    AUTORIZADO("A"),
    DESAUTORIZADO("D");

    private final String codigo;

    TipoAcesso(String codigo) {
        this.codigo = codigo;
    }

    public static TipoAcesso fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }
        
        for (TipoAcesso tipoAcesso : TipoAcesso.values()) {
            if (tipoAcesso.getCodigo().equals(codigo)) {
                return tipoAcesso;
            }
        }
        throw new IllegalArgumentException("Código de tipo de acesso inválido: " + codigo);
    }
}