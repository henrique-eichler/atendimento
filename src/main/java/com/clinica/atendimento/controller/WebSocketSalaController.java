package com.clinica.atendimento.controller;

import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.model.TerapiaSala;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.repository.TerapiaSalaRepository;
import jakarta.transaction.Transactional;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class WebSocketSalaController {

    private final SalaRepository salaRepository;
    private final TerapiaSalaRepository terapiaSalaRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSalaController(SalaRepository salaRepository,
                                   TerapiaSalaRepository terapiaSalaRepository,
                                   SimpMessagingTemplate messagingTemplate) {
        this.salaRepository = salaRepository;
        this.terapiaSalaRepository = terapiaSalaRepository;
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

        // If it's an existing sala, delete all existing terapia associations
        if (!novo) {
            terapiaSalaRepository.deleteBySala(salvo);
        }

        // Create new terapia associations
        if (salaDTO.getTerapias() != null) {
            List<TerapiaSala> terapiaSalas = salaDTO.getTerapias().stream()
                    .map(terapiaDTO -> {
                        Terapia terapia = terapiaDTO.toEntity();
                        return TerapiaSala.builder()
                                .sala(salvo)
                                .terapia(terapia)
                                .build();
                    })
                    .collect(Collectors.toList());

            terapiaSalaRepository.saveAll(terapiaSalas);
        }

        // Reload the sala to get the updated terapias
        Sala reloaded = salaRepository.findById(salvo.id()).orElse(salvo);
        messagingTemplate.convertAndSend(novo ? "/topic/sala/retorno/salvar" : "/topic/sala/retorno/editar", SalaDTO.fromEntity(reloaded));
    }

    @MessageMapping("/sala/excluir")
    @Transactional
    public void deletarSala(@Payload Long id) {
        // Delete all terapia associations first
        salaRepository.findById(id).ifPresent(terapiaSalaRepository::deleteBySala);

        salaRepository.deleteById(id);
        messagingTemplate.convertAndSend("/topic/sala/retorno/excluir", id);
    }
}
