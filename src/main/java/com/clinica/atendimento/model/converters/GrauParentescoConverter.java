package com.clinica.atendimento.model.converters;

import com.clinica.atendimento.model.enums.GrauParentesco;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class GrauParentescoConverter implements AttributeConverter<GrauParentesco, String> {

    @Override
    public String convertToDatabaseColumn(GrauParentesco attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public GrauParentesco convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return GrauParentesco.fromCodigo(dbData);
    }
}