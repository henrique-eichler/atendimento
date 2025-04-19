package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Sessao;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDTO {

    private Long id;
    private String textoOriginal;
    private String transcricao;
    private String interpretacao;
    private LocalDateTime registradaEm;
    private List<AudioDTO> audios = new ArrayList<>();

    public static SessaoDTO fromEntity(Sessao sessao) {
        if (sessao == null) {
            return null;
        }
        
        SessaoDTO dto = new SessaoDTO();
        dto.setId(sessao.getId());
        dto.setTextoOriginal(sessao.getTextoOriginal());
        dto.setTranscricao(sessao.getTranscricao());
        dto.setInterpretacao(sessao.getInterpretacao());
        dto.setRegistradaEm(sessao.getRegistradaEm());
        
        if (sessao.getAudios() != null) {
            dto.setAudios(sessao.getAudios().stream()
                .map(AudioDTO::fromEntity)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    public Sessao toEntity() {
        Sessao sessao = new Sessao();
        sessao.setId(this.id);
        sessao.setTextoOriginal(this.textoOriginal);
        sessao.setTranscricao(this.transcricao);
        sessao.setInterpretacao(this.interpretacao);
        sessao.setRegistradaEm(this.registradaEm);
        return sessao;
    }
}