package com.clinica.atendimento.model.converters

import com.clinica.atendimento.model.enums.GrauParentesco
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter
class GrauParentescoConverter : AttributeConverter<GrauParentesco, String> {

    override fun convertToDatabaseColumn(attribute: GrauParentesco?): String? {
        return attribute?.codigo
    }

    override fun convertToEntityAttribute(dbData: String?): GrauParentesco? {
        return if (dbData == null) null else GrauParentesco.fromCodigo(dbData)
    }
}