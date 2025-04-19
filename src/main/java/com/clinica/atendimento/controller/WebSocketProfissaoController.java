package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.ProfissaoDTO;
import com.clinica.atendimento.model.Profissao;
import com.clinica.atendimento.repository.ProfissaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import java.util.stream.Collectors;

@Controller
public class WebSocketProfissaoController {

    @Autowired private ProfissaoRepository profissaoRepository;
    @Autowired private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/profissao/listar")
    public void listarProfissoes() {
        var todos = profissaoRepository.findAll()
                .stream()
                .map(ProfissaoDTO::fromEntity)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/profissao/retorno/listar", todos);
    }

    @MessageMapping("/profissao/salvar")
    public void cadastrarProfissao(@Payload ProfissaoDTO profissaoDTO) {
        boolean novo = profissaoDTO.getId() == null;
        Profissao profissao = profissaoDTO.toEntity();
        var salvo = profissaoRepository.save(profissao);
        messagingTemplate.convertAndSend(novo ? "/topic/profissao/retorno/salvar" : "/topic/profissao/retorno/editar", ProfissaoDTO.fromEntity(salvo));
    }

    @MessageMapping("/profissao/excluir")
    public void deletarProfissao(@Payload Long id) {
        profissaoRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/profissao/retorno/excluir", id);
    }
}
