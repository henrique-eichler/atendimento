package com.clinica.atendimento.model.enums

enum class DiaSemana(val codigo: String) {
    DOMINGO("D"),
    SEGUNDA("2"),
    TERCA("3"),
    QUARTA("4"),
    QUINTA("5"),
    SEXTA("6"),
    SABADO("S");

    companion object {
        @JvmStatic
        fun fromCodigo(codigo: String?): DiaSemana? {
            if (codigo == null) {
                return null
            }

            for (diaSemana in values()) {
                if (diaSemana.codigo == codigo) {
                    return diaSemana
                }
            }
            throw IllegalArgumentException("Código de dia da semana inválido: $codigo")
        }
    }
}