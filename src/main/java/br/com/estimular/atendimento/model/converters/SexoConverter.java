package br.com.estimular.atendimento.model.converters;

import br.com.estimular.atendimento.model.enums.Sexo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SexoConverter implements AttributeConverter<Sexo, String> {

    @Override
    public String convertToDatabaseColumn(Sexo attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCodigo();
    }

    @Override
    public Sexo convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Sexo.fromCodigo(dbData);
    }
}