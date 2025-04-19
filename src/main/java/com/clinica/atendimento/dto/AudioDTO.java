package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Audio;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AudioDTO {

    private Long id;
    private String caminho;
    private Long duracaoSegundos;
    private Long sessaoId;

    public static AudioDTO fromEntity(Audio audio) {
        if (audio == null) {
            return null;
        }
        
        AudioDTO dto = new AudioDTO();
        dto.setId(audio.getId());
        dto.setCaminho(audio.getCaminho());
        dto.setDuracaoSegundos(audio.getDuracaoSegundos());
        
        if (audio.getSessao() != null) {
            dto.setSessaoId(audio.getSessao().getId());
        }
        
        return dto;
    }

    public Audio toEntity() {
        Audio audio = new Audio();
        audio.setId(this.id);
        audio.setCaminho(this.caminho);
        audio.setDuracaoSegundos(this.duracaoSegundos);
        return audio;
    }
}