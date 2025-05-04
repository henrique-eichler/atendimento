package com.clinica.atendimento.model.converters;

import com.clinica.atendimento.model.enums.DiaSemana;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class DiaSemanaConverter implements AttributeConverter<DiaSemana, String> {

    @Override
    public String convertToDatabaseColumn(DiaSemana attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public DiaSemana convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return DiaSemana.fromCodigo(dbData);
    }
}