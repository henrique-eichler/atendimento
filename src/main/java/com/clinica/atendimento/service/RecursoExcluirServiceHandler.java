package com.clinica.atendimento.service;

import com.clinica.atendimento.repository.RecursoRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class RecursoExcluirServiceHandler extends AbstractServiceHandler<Long> {

    private final WebSocketHandler webSocketHandler;
    private final RecursoRepository recursoRepository;

    public RecursoExcluirServiceHandler(WebSocketHandler webSocketHandler, RecursoRepository recursoRepository) {
        super(Long.class);
        this.webSocketHandler = webSocketHandler;
        this.recursoRepository = recursoRepository;

        this.webSocketHandler.register("/topic/recurso/request/delete", this);
    }

    @Override
    public void process(WebSocketSession session, Long id) throws IOException {
        recursoRepository.deleteById(id);
        this.webSocketHandler.sendToSession(session, "/topic/recurso/response/delete", id);
    }
}
