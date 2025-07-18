package br.com.estimular.atendimento.service;

import br.com.estimular.atendimento.dto.ProfissionalDTO;
import br.com.estimular.atendimento.model.Pessoa;
import br.com.estimular.atendimento.model.Profissional;
import br.com.estimular.atendimento.model.ProfissionalTerapia;
import br.com.estimular.atendimento.model.Terapia;
import br.com.estimular.atendimento.repository.PessoaRepository;
import br.com.estimular.atendimento.repository.ProfissionalRepository;
import br.com.estimular.atendimento.repository.ProfissionalTerapiaRepository;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class ProfissionalSalvarServiceHandler extends AbstractServiceHandler<ProfissionalDTO> {

    private final WebSocketHandler webSocketHandler;
    private final PessoaRepository pessoaRepository;
    private final ProfissionalRepository profissionalRepository;
    private final ProfissionalTerapiaRepository profissionalTerapiaRepository;

    ProfissionalSalvarServiceHandler(WebSocketHandler webSocketHandler, PessoaRepository pessoaRepository, ProfissionalRepository profissionalRepository, ProfissionalTerapiaRepository profissionalTerapiaRepository) {
        super(ProfissionalDTO.class);
        this.webSocketHandler = webSocketHandler;
        this.pessoaRepository = pessoaRepository;
        this.profissionalRepository = profissionalRepository;
        this.profissionalTerapiaRepository = profissionalTerapiaRepository;

        this.webSocketHandler.register("/topic/profissional/request/save", this);
    }

    @Override
    @Transactional()
    public void process(WebSocketSession session, ProfissionalDTO profissionalDTO) throws IOException {
        Pessoa pessoa = pessoaRepository.save(profissionalDTO.pessoa().toEntity());
        Profissional profissional = profissionalRepository.save(profissionalDTO.toEntity().pessoa(pessoa).id(pessoa.id()));

        List<ProfissionalTerapia> terapias = profissionalTerapiaRepository.findByProfissional(profissional);
        terapias.stream().filter(ts -> profissionalDTO.terapias().stream().noneMatch(t -> t.id().equals(ts.terapia().id()))).forEach(profissionalTerapiaRepository::delete);
        profissionalDTO.terapias().stream().filter(t -> terapias.stream().noneMatch(pt -> pt.terapia().id().equals(t.id()))).map(t -> ProfissionalTerapia.builder().dataValidade(LocalDate.now().plusYears(1)).profissional(profissional).terapia(Terapia.builder().id(t.id()).build()).build()).forEach(profissionalTerapiaRepository::save);

        ProfissionalDTO profissionalSaved = ProfissionalDTO.fromEntity(profissionalRepository.findProfissionalById(profissional.id()));

        this.webSocketHandler.sendToSession(session, "/topic/profissional/response/save", profissionalSaved);
    }
}
