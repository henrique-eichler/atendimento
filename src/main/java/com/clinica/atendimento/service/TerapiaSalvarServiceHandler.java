package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.TerapiaDTO;
import com.clinica.atendimento.repository.TerapiaRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class TerapiaSalvarServiceHandler extends AbstractServiceHandler<TerapiaDTO> {

    private final WebSocketHandler webSocketHandler;
    private final TerapiaRepository terapiaRepository;

    TerapiaSalvarServiceHandler(WebSocketHandler webSocketHandler, TerapiaRepository terapiaRepository) {
        super(TerapiaDTO.class);
        this.webSocketHandler = webSocketHandler;
        this.terapiaRepository = terapiaRepository;

        this.webSocketHandler.register("/topic/terapia/request/save", this);
    }

    @Override
    public void process(WebSocketSession session, TerapiaDTO terapiaDTO) throws IOException {
        TerapiaDTO terapiaSaved = TerapiaDTO.fromEntity(terapiaRepository.save(terapiaDTO.toEntity()));
        this.webSocketHandler.sendToSession(session, "/topic/terapia/response/save", terapiaSaved);
    }
}
