package com.clinica.atendimento.model.enums

enum class GrauParentesco(val codigo: String) {
    PAI("P"),
    MAE("M"),
    OUTRO("O");

    companion object {
        @JvmStatic
        fun fromCodigo(codigo: String?): GrauParentesco? {
            if (codigo == null) {
                return null
            }

            for (grauParentesco in values()) {
                if (grauParentesco.codigo == codigo) {
                    return grauParentesco
                }
            }
            throw IllegalArgumentException("Código de grau de parentesco inválido: $codigo")
        }
    }
}