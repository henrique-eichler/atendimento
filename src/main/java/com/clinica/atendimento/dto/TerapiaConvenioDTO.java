package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Convenio;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.model.TerapiaConvenio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TerapiaConvenioDTO {
    private Long id;
    private TerapiaDTO terapia;
    private ConvenioDTO convenio;
    private Float valorTerapia;
    private Float valorProfissional;

    // Static method to convert from entity to DTO
    public static TerapiaConvenioDTO fromEntity(TerapiaConvenio terapiaConvenio) {
        if (terapiaConvenio == null) {
            return null;
        }

        return TerapiaConvenioDTO.builder()
            .id(terapiaConvenio.id())
            .terapia(TerapiaDTO.fromEntity(terapiaConvenio.terapia()))
            .convenio(ConvenioDTO.fromEntity(terapiaConvenio.convenio()))
            .valorTerapia(terapiaConvenio.valorTerapia())
            .valorProfissional(terapiaConvenio.valorProfissional())
            .build();
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
