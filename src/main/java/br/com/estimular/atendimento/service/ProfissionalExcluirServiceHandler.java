package br.com.estimular.atendimento.service;

import br.com.estimular.atendimento.repository.ProfissionalRepository;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class ProfissionalExcluirServiceHandler extends AbstractServiceHandler<Long> {

    private final WebSocketHandler webSocketHandler;
    private final ProfissionalRepository profissionalRepository;

    public ProfissionalExcluirServiceHandler(WebSocketHandler webSocketHandler, ProfissionalRepository profissionalRepository) {
        super(Long.class);
        this.webSocketHandler = webSocketHandler;
        this.profissionalRepository = profissionalRepository;

        this.webSocketHandler.register("/topic/profissional/request/delete", this);
    }

    @Override
    @Transactional()
    public void process(WebSocketSession session, Long id) throws IOException {
        profissionalRepository.deleteById(id);
        this.webSocketHandler.sendToSession(session, "/topic/profissional/response/delete", id);
    }
}
