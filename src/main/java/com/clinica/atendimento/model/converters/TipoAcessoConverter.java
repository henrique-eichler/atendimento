package com.clinica.atendimento.model.converters;

import com.clinica.atendimento.model.enums.TipoAcesso;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class TipoAcessoConverter implements AttributeConverter<TipoAcesso, String> {

    @Override
    public String convertToDatabaseColumn(TipoAcesso attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public TipoAcesso convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return TipoAcesso.fromCodigo(dbData);
    }
}