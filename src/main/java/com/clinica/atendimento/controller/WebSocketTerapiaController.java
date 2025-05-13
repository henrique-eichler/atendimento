package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.TerapiaDTO;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.repository.TerapiaRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
public class WebSocketTerapiaController {

    private final TerapiaRepository terapiaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketTerapiaController(TerapiaRepository terapiaRepository, SimpMessagingTemplate messagingTemplate) {
        this.terapiaRepository = terapiaRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/terapia/listar")
    public void listarProfissoes() {
        var todos = terapiaRepository.findAll()
                .stream()
                .map(TerapiaDTO::fromEntity)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/terapia/retorno/listar", todos);
    }

    @MessageMapping("/terapia/salvar")
    public void cadastrarTerapia(@Payload TerapiaDTO terapiaDTO) {
        boolean novo = terapiaDTO.getId() == null;
        Terapia terapia = terapiaDTO.toEntity();
        var salvo = terapiaRepository.save(terapia);
        messagingTemplate.convertAndSend(novo ? "/topic/terapia/retorno/salvar" : "/topic/terapia/retorno/editar", TerapiaDTO.fromEntity(salvo));
    }

    @MessageMapping("/terapia/excluir")
    public void deletarTerapia(@Payload Long id) {
        terapiaRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/terapia/retorno/excluir", id);
    }
}
