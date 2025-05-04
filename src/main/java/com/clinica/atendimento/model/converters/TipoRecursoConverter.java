package com.clinica.atendimento.model.converters;

import com.clinica.atendimento.model.enums.TipoRecurso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoRecursoConverter implements AttributeConverter<TipoRecurso, String> {

    @Override
    public String convertToDatabaseColumn(TipoRecurso attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public TipoRecurso convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoRecurso.fromCodigo(dbData);
    }
}