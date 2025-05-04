package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Convenio;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.model.TerapiaConvenio;

public record TerapiaConvenioDTO(
    Long id,
    TerapiaDTO terapia,
    ConvenioDTO convenio,
    Float valorTerapia,
    Float valorProfissional
) {
    // Static method to convert from entity to DTO
    public static TerapiaConvenioDTO fromEntity(TerapiaConvenio terapiaConvenio) {
        if (terapiaConvenio == null) {
            return null;
        }

        return new TerapiaConvenioDTO(
            terapiaConvenio.id(),
            TerapiaDTO.fromEntity(terapiaConvenio.terapia()),
            ConvenioDTO.fromEntity(terapiaConvenio.convenio()),
            terapiaConvenio.valorTerapia(),
            terapiaConvenio.valorProfissional()
        );
    }

    // Method to convert from DTO to entity
    public TerapiaConvenio toEntity() {
        Terapia terapiaEntity = terapia != null ? terapia.toEntity() : null;
        Convenio convenioEntity = convenio != null ? convenio.toEntity() : null;

        return TerapiaConvenio.builder()
            .id(id)
            .terapia(terapiaEntity)
            .convenio(convenioEntity)
            .valorTerapia(valorTerapia)
            .valorProfissional(valorProfissional)
            .build();
    }
}