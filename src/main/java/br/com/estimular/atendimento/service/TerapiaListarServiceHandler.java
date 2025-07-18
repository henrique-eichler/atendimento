package br.com.estimular.atendimento.service;

import br.com.estimular.atendimento.dto.TerapiaDTO;
import br.com.estimular.atendimento.repository.TerapiaRepository;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    public void process(WebSocketSession session, Void unused) throws IOException {
        List<TerapiaDTO> list = terapiaRepository.findAll().stream().map(TerapiaDTO::fromEntity).toList();
        this.webSocketHandler.sendToSession(session, "/topic/terapia/response/list", list);
    }
}
