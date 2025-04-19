package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Responsavel;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsavelDTO {

    private Long id;
    private String nome;
    private Set<Long> pacientesIds = new HashSet<>();

    public static ResponsavelDTO fromEntity(Responsavel responsavel) {
        ResponsavelDTO dto = new ResponsavelDTO();
        dto.setId(responsavel.getId());
        dto.setNome(responsavel.getNome());
        
        if (responsavel.getPacientes() != null) {
            dto.setPacientesIds(responsavel.getPacientes().stream()
                .map(paciente -> paciente.getId())
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }

    public Responsavel toEntity() {
        Responsavel responsavel = new Responsavel();
        responsavel.setId(this.id);
        responsavel.setNome(this.nome);
        return responsavel;
    }
}