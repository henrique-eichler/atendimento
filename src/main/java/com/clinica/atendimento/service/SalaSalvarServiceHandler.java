package com.clinica.atendimento.service;

import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.model.*;
import com.clinica.atendimento.repository.RecursoSalaRepository;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.repository.TerapiaSalaRepository;
import com.clinica.atendimento.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

@Service
public class SalaSalvarServiceHandler extends AbstractServiceHandler<SalaDTO> {

    private final WebSocketHandler webSocketHandler;
    private final SalaRepository salaRepository;
    private final TerapiaSalaRepository terapiaSalaRepository;
    private final RecursoSalaRepository recursoSalaRepository;

    SalaSalvarServiceHandler(WebSocketHandler webSocketHandler, SalaRepository salaRepository, TerapiaSalaRepository terapiaSalaRepository, RecursoSalaRepository recursoSalaRepository) {
        super(SalaDTO.class);
        this.webSocketHandler = webSocketHandler;
        this.salaRepository = salaRepository;
        this.terapiaSalaRepository = terapiaSalaRepository;
        this.recursoSalaRepository = recursoSalaRepository;

        this.webSocketHandler.register("/topic/sala/request/save", this);
    }

    @Override
    public void process(WebSocketSession session, SalaDTO salaDTO) throws IOException {
        Sala sala = salaRepository.save(salaDTO.toEntity());

        List<TerapiaSala> terapias = terapiaSalaRepository.findBySala(sala);
        terapias.stream().filter(ts -> salaDTO.terapias().stream().anyMatch(t -> t.id().equals(ts.terapia().id()))).forEach(terapiaSalaRepository::delete);
        salaDTO.terapias().stream().filter(t -> terapias.stream().noneMatch(ts -> ts.terapia().id().equals(t.id()))).map(t -> TerapiaSala.builder().sala(sala).terapia(Terapia.builder().id(t.id()).build()).build()).forEach(terapiaSalaRepository::save);

        List<RecursoSala> recursos = recursoSalaRepository.findBySala(sala);
        recursos.stream().filter(rs -> salaDTO.recursos().stream().anyMatch(r -> r.id().equals(rs.recurso().id()))).forEach(recursoSalaRepository::delete);
        salaDTO.recursos().stream().filter(r -> recursos.stream().noneMatch(rs -> rs.recurso().id().equals(r.id()))).map(r -> RecursoSala.builder().sala(sala).recurso(Recurso.builder().id(r.id()).build()).build()).forEach(recursoSalaRepository::save);

        SalaDTO salaSaved = SalaDTO.fromEntity(sala);
        salaSaved.terapias(salaDTO.terapias());
        salaSaved.recursos(salaDTO.recursos());

        this.webSocketHandler.sendToSession(session, "/topic/sala/response/save", salaSaved);
    }
}
