package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Profissional;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfissionalDTO {

    private Long id;
    private String nome;
    private Long profissaoId;
    private String profissaoNome;

    public static ProfissionalDTO fromEntity(Profissional profissional) {
        if (profissional == null) {
            return null;
        }
        
        ProfissionalDTO dto = new ProfissionalDTO();
        dto.setId(profissional.getId());
        dto.setNome(profissional.getNome());
        
        if (profissional.getProfissao() != null) {
            dto.setProfissaoId(profissional.getProfissao().getId());
            dto.setProfissaoNome(profissional.getProfissao().getNome());
        }
        
        return dto;
    }

    public Profissional toEntity() {
        Profissional profissional = new Profissional();
        profissional.setId(this.id);
        profissional.setNome(this.nome);
        return profissional;
    }
}