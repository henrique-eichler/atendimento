package com.clinica.atendimento.model.enums

enum class Sexo(val codigo: String) {
    FEMININO("F"),
    MASCULINO("M");

    companion object {
        @JvmStatic
        fun fromCodigo(codigo: String?): Sexo? {
            if (codigo == null) {
                return null
            }

            for (sexo in values()) {
                if (sexo.codigo == codigo) {
                    return sexo
                }
            }
            throw IllegalArgumentException("Código de sexo inválido: $codigo")
        }
    }
}