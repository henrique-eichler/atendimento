package br.com.estimular.atendimento.service;

import br.com.estimular.atendimento.dto.ProfissionalDTO;
import br.com.estimular.atendimento.repository.ProfissionalRepository;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class ProfissionalListarServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;
    private final ProfissionalRepository profissionalRepository;

    public ProfissionalListarServiceHandler(WebSocketHandler webSocketHandler, ProfissionalRepository profissionalRepository) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;
        this.profissionalRepository = profissionalRepository;

        this.webSocketHandler.register("/topic/profissional/request/list", this);
    }

    @Override
    @Transactional(readOnly = true)
    public void process(WebSocketSession session, Void unused) throws IOException {
        List<ProfissionalDTO> list = profissionalRepository.findAllProfissionais().stream().map(ProfissionalDTO::fromEntity).toList();
        this.webSocketHandler.sendToSession(session, "/topic/profissional/response/list", list);
    }
}
