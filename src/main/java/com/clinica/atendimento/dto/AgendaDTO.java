package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Agenda;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AgendaDTO {

    private Long id;
    private Long pacienteId;
    private String pacienteNome;
    private Long responsavelId;
    private String responsavelNome;
    private Long profissionalId;
    private String profissionalNome;
    private LocalDateTime dataHora;
    private SessaoDTO sessao;

    public static AgendaDTO fromEntity(Agenda agenda) {
        if (agenda == null) {
            return null;
        }
        
        AgendaDTO dto = new AgendaDTO();
        dto.setId(agenda.getId());
        dto.setDataHora(agenda.getDataHora());
        
        if (agenda.getPaciente() != null) {
            dto.setPacienteId(agenda.getPaciente().getId());
            dto.setPacienteNome(agenda.getPaciente().getNome());
        }
        
        if (agenda.getResponsavel() != null) {
            dto.setResponsavelId(agenda.getResponsavel().getId());
            dto.setResponsavelNome(agenda.getResponsavel().getNome());
        }
        
        if (agenda.getProfissional() != null) {
            dto.setProfissionalId(agenda.getProfissional().getId());
            dto.setProfissionalNome(agenda.getProfissional().getNome());
        }
        
        if (agenda.getSessao() != null) {
            dto.setSessao(SessaoDTO.fromEntity(agenda.getSessao()));
        }
        
        return dto;
    }

    public Agenda toEntity() {
        Agenda agenda = new Agenda();
        agenda.setId(this.id);
        agenda.setDataHora(this.dataHora);
        return agenda;
    }
}