package com.clinica.atendimento.service;

import com.clinica.atendimento.repository.TerapiaRepository;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class TerapiaExcluirServiceHandler extends AbstractServiceHandler<Long> {

    private final WebSocketHandler webSocketHandler;
    private final TerapiaRepository terapiaRepository;

    public TerapiaExcluirServiceHandler(WebSocketHandler webSocketHandler, TerapiaRepository terapiaRepository) {
        super(Long.class);
        this.webSocketHandler = webSocketHandler;
        this.terapiaRepository = terapiaRepository;

        this.webSocketHandler.register("/topic/terapia/request/delete", this);
    }

    @Override
    @Transactional()
    public void process(WebSocketSession session, Long id) throws IOException {
        terapiaRepository.deleteById(id);
        this.webSocketHandler.sendToSession(session, "/topic/terapia/response/delete", id);
    }
}
