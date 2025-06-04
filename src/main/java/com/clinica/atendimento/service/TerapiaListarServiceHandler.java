package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.TerapiaDTO;
import com.clinica.atendimento.repository.TerapiaRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class TerapiaListarServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;
    private final TerapiaRepository terapiaRepository;

    public TerapiaListarServiceHandler(WebSocketHandler webSocketHandler, TerapiaRepository terapiaRepository) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;
        this.terapiaRepository = terapiaRepository;

        this.webSocketHandler.register("/topic/terapia/request/list", this);
    }

    @Override
    public void process(WebSocketSession session, Void unused) throws IOException {
        List<TerapiaDTO> list = terapiaRepository.findAll().stream().map(TerapiaDTO::fromEntity).toList();
        this.webSocketHandler.sendToSession(session, "/topic/terapia/response/list", list);
    }
}
