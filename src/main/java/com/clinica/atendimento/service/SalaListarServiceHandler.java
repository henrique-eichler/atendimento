package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class SalaListarServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;
    private final SalaRepository salaRepository;

    public SalaListarServiceHandler(WebSocketHandler webSocketHandler, SalaRepository salaRepository) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;
        this.salaRepository = salaRepository;

        this.webSocketHandler.register("/topic/sala/request/list", this);
    }

    @Override
    public void process(WebSocketSession session, Void unused) throws IOException {
        List<SalaDTO> list = salaRepository.findAllSalas().stream().map(SalaDTO::fromEntity).toList();
        this.webSocketHandler.sendToSession(session, "/topic/sala/response/list", list);
    }
}
