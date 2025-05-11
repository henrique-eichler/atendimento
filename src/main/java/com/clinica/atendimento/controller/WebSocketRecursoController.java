package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.RecursoDTO;
import com.clinica.atendimento.model.Recurso;
import com.clinica.atendimento.repository.RecursoRepository;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

@Controller
public class WebSocketRecursoController {

    private final RecursoRepository recursoRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketRecursoController(RecursoRepository recursoRepository, SimpMessagingTemplate messagingTemplate) {
        this.recursoRepository = recursoRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/recurso/listar")
    public void listarRecursos() {
        var todos = recursoRepository.findAll()
                .stream()
                .map(RecursoDTO::fromEntity)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/recurso/retorno/listar", todos);
    }

    @MessageMapping("/recurso/salvar")
    public void cadastrarRecurso(@Payload RecursoDTO recursoDTO) {
        boolean novo = recursoDTO.getId() == null;
        Recurso recurso = recursoDTO.toEntity();
        var salvo = recursoRepository.save(recurso);
        messagingTemplate.convertAndSend(novo ? "/topic/recurso/retorno/salvar" : "/topic/recurso/retorno/editar", RecursoDTO.fromEntity(salvo));
    }

    @MessageMapping("/recurso/excluir")
    public void deletarRecurso(@Payload Long id) {
        recursoRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/recurso/retorno/excluir", id);
    }
}