package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.RecursoDTO;
import com.clinica.atendimento.repository.RecursoRepository;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class RecursoListarServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;
    private final RecursoRepository recursoRepository;

    public RecursoListarServiceHandler(WebSocketHandler webSocketHandler, RecursoRepository recursoRepository) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;
        this.recursoRepository = recursoRepository;

        this.webSocketHandler.register("/topic/recurso/request/list", this);
    }

    @Override
    @Transactional(readOnly = true)
    public void process(WebSocketSession session, Void unused) throws IOException {
        List<RecursoDTO> list = recursoRepository.findAll().stream().map(RecursoDTO::fromEntity).toList();
        this.webSocketHandler.sendToSession(session, "/topic/recurso/response/list", list);
    }
}
