package br.com.estimular.atendimento.model.enums;

import lombok.Getter;

@Getter
public enum GrauParentesco {
    PAI("P"),
    MAE("M"),
    OUTRO("O");

    private final String codigo;

    GrauParentesco(String codigo) {
        this.codigo = codigo;
    }

    public static GrauParentesco fromCodigo(String codigo) {
        if (codigo == null) {
            return null;
        }

        for (GrauParentesco grauParentesco : GrauParentesco.values()) {
            if (grauParentesco.getCodigo().equals(codigo)) {
                return grauParentesco;
            }
        }
        throw new IllegalArgumentException("Código de grau de parentesco inválido: " + codigo);
    }
}