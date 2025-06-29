package com.clinica.atendimento.model.converters

import com.clinica.atendimento.model.enums.Sexo
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class SexoConverter : AttributeConverter<Sexo, String> {

    override fun convertToDatabaseColumn(attribute: Sexo?): String? {
        return attribute?.codigo
    }

    override fun convertToEntityAttribute(dbData: String?): Sexo? {
        return if (dbData == null) null else Sexo.fromCodigo(dbData)
    }
}