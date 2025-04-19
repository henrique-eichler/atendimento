package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.PacienteDTO;
import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.repository.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

@Controller
public class WebSocketPacienteController {

    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/paciente/listar")
    public void listarPacientes() {
        var todos = pacienteRepository.findAllWithResponsaveis()
            .stream()
            .map(PacienteDTO::fromEntity)
            .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/paciente/retorno/listar", todos);
    }

    @MessageMapping("/paciente/salvar")
    public void cadastrarPaciente(@Payload PacienteDTO pacienteDTO) {
        boolean novo = pacienteDTO.getId() == null;
        Paciente paciente = pacienteDTO.toEntity();
        var salvo = pacienteRepository.save(paciente);
        messagingTemplate.convertAndSend(novo ? "/topic/paciente/retorno/salvar" : "/topic/paciente/retorno/editar", PacienteDTO.fromEntity(salvo));
    }

    @MessageMapping("/paciente/excluir")
    public void deletarPaciente(@Payload Long id) {
        pacienteRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/paciente/retorno/excluir", id);
    }
}
