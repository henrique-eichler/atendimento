package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.RecursoDTO;
import com.clinica.atendimento.repository.RecursoRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class RecursoSalvarServiceHandler extends AbstractServiceHandler<RecursoDTO> {

    private final WebSocketHandler webSocketHandler;
    private final RecursoRepository recursoRepository;

    RecursoSalvarServiceHandler(WebSocketHandler webSocketHandler, RecursoRepository recursoRepository) {
        super(RecursoDTO.class);
        this.webSocketHandler = webSocketHandler;
        this.recursoRepository = recursoRepository;

        this.webSocketHandler.register("/topic/recurso/request/save", this);
    }

    @Override
    @Transactional()
    public void process(WebSocketSession session, RecursoDTO recursoDTO) throws IOException {
        RecursoDTO recursoSaved = RecursoDTO.fromEntity(recursoRepository.save(recursoDTO.toEntity()));
        this.webSocketHandler.sendToSession(session, "/topic/recurso/response/save", recursoSaved);
    }
}
