package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.repository.SalaRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

@Controller
public class WebSocketSalaController {

    private final SalaRepository salaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSalaController(SalaRepository salaRepository, SimpMessagingTemplate messagingTemplate) {
        this.salaRepository = salaRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sala/listar")
    public void listarSalas() {
        var todos = salaRepository.findAll()
                .stream()
                .map(SalaDTO::fromEntity)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/sala/retorno/listar", todos);
    }

    @MessageMapping("/sala/salvar")
    public void cadastrarSala(@Payload SalaDTO salaDTO) {
        boolean novo = salaDTO.id() == null;
        Sala sala = salaDTO.toEntity();
        var salvo = salaRepository.save(sala);
        messagingTemplate.convertAndSend(novo ? "/topic/sala/retorno/salvar" : "/topic/sala/retorno/editar", SalaDTO.fromEntity(salvo));
    }

    @MessageMapping("/sala/excluir")
    public void deletarSala(@Payload Long id) {
        salaRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/sala/retorno/excluir", id);
    }
}