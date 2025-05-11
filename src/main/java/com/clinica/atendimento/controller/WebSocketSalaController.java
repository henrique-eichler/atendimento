package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.TerapiaSala;
import com.clinica.atendimento.repository.RecursoSalaRepository;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.repository.TerapiaSalaRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.stream.Collectors;

@Controller
public class WebSocketSalaController {

    private final SalaRepository salaRepository;
    private final TerapiaSalaRepository terapiaSalaRepository;
    private final RecursoSalaRepository recursoSalaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSalaController(SalaRepository salaRepository,
                                   TerapiaSalaRepository terapiaSalaRepository,
                                   RecursoSalaRepository recursoSalaRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.salaRepository = salaRepository;
        this.terapiaSalaRepository = terapiaSalaRepository;
        this.recursoSalaRepository = recursoSalaRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sala/listar")
    public void listarSalas() {
        var todos = salaRepository.findAllSalas()
                .stream()
                .map(SalaDTO::fromEntity)
                .collect(Collectors.toList());
        messagingTemplate.convertAndSend("/topic/sala/retorno/listar", todos);
    }

    @MessageMapping("/sala/salvar")
    @Transactional
    public void cadastrarSala(@Payload SalaDTO salaDTO) {
        boolean novo = salaDTO.getId() == null;
        Sala sala = salaDTO.toEntity();
        var salvo = salaRepository.save(sala);

        if (!novo) {
            terapiaSalaRepository.deleteBySala(salvo);
            recursoSalaRepository.deleteBySala(salvo);
        }

        if (salaDTO.getTerapias() != null) {
            salaDTO.getTerapias()
                    .stream()
                    .map(terapiaDTO -> TerapiaSala.builder().sala(salvo).terapia(terapiaDTO.toEntity()).build())
                    .forEach(terapiaSalaRepository::save);
        }

        if (salaDTO.getRecursos() != null) {
            salaDTO.getRecursos()
                    .stream()
                    .map(recursoDTO -> {
                        RecursoSala recursoSala = new RecursoSala();
                        recursoSala.setSala(salvo);
                        recursoSala.setRecurso(recursoDTO.toEntity());
                        return recursoSala;
                    })
                    .forEach(recursoSalaRepository::save);
        }

        Sala reloaded = salaRepository.findById(salvo.id()).orElse(salvo);
        messagingTemplate.convertAndSend(novo ? "/topic/sala/retorno/salvar" : "/topic/sala/retorno/editar", SalaDTO.fromEntity(reloaded));
    }

    @MessageMapping("/sala/excluir")
    @Transactional
    public void deletarSala(@Payload Long id) {
        salaRepository.findById(id).ifPresent(sala -> {
            terapiaSalaRepository.deleteBySala(sala);
            recursoSalaRepository.deleteBySala(sala);
        });

        salaRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/sala/retorno/excluir", id);
    }
}
