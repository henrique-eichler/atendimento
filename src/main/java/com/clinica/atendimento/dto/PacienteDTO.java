package com.clinica.atendimento.dto;

import com.clinica.atendimento.model.Paciente;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PacienteDTO {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private String sexo;
    private Set<Long> responsaveisIds = new HashSet<>();

    public static PacienteDTO fromEntity(Paciente paciente) {
        PacienteDTO dto = new PacienteDTO();
        dto.setId(paciente.getId());
        dto.setNome(paciente.getNome());
        dto.setDataNascimento(paciente.getDataNascimento());
        dto.setSexo(paciente.getSexo() != null ? paciente.getSexo().name() : null);
        
        if (paciente.getResponsaveis() != null) {
            dto.setResponsaveisIds(paciente.getResponsaveis().stream()
                .map(responsavel -> responsavel.getId())
                .collect(Collectors.toSet()));
        }
        
        return dto;
    }

    public Paciente toEntity() {
        Paciente paciente = new Paciente();
        paciente.setId(this.id);
        paciente.setNome(this.nome);
        paciente.setDataNascimento(this.dataNascimento);
        
        if (this.sexo != null) {
            paciente.setSexo(Paciente.Sexo.valueOf(this.sexo));
        }
        
        return paciente;
    }
}