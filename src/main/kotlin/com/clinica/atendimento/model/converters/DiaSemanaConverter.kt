package com.clinica.atendimento.model.converters

import com.clinica.atendimento.model.enums.DiaSemana
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class DiaSemanaConverter : AttributeConverter<DiaSemana, String> {

    override fun convertToDatabaseColumn(attribute: DiaSemana?): String? {
        return attribute?.codigo
    }

    override fun convertToEntityAttribute(dbData: String?): DiaSemana? {
        return if (dbData == null) null else DiaSemana.fromCodigo(dbData)
    }
}