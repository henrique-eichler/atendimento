package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Profissao;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfissaoDTO {

    private Long id;
    private String nome;

    public static ProfissaoDTO fromEntity(Profissao profissao) {
        if (profissao == null) {
            return null;
        }
        
        ProfissaoDTO dto = new ProfissaoDTO();
        dto.setId(profissao.getId());
        dto.setNome(profissao.getNome());
        return dto;
    }

    public Profissao toEntity() {
        Profissao profissao = new Profissao();
        profissao.setId(this.id);
        profissao.setNome(this.nome);
        return profissao;
    }
}