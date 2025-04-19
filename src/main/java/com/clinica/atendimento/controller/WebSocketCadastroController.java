
package com.clinica.atendimento.controller;

import com.clinica.atendimento.model.Agenda;
import com.clinica.atendimento.model.Paciente;
import com.clinica.atendimento.model.Profissional;
import com.clinica.atendimento.model.Responsavel;
import com.clinica.atendimento.repository.AgendaRepository;
import com.clinica.atendimento.repository.PacienteRepository;
import com.clinica.atendimento.repository.ProfissionalRepository;
import com.clinica.atendimento.repository.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketCadastroController {

    @Autowired
    private ResponsavelRepository responsavelRepository;
    @Autowired
    private ProfissionalRepository profissionalRepository;
    @Autowired
    private AgendaRepository agendaRepository;

    @MessageMapping("/cadastrar-responsavel")
    public void cadastrarResponsavel(@Payload Responsavel responsavel, @Header("simpSessionId") String sessionId) {
        responsavelRepository.save(responsavel);
    }

    @MessageMapping("/cadastrar-profissional")
    public void cadastrarProfissional(@Payload Profissional profissional, @Header("simpSessionId") String sessionId) {
        profissionalRepository.save(profissional);
    }

    @MessageMapping("/cadastrar-agenda")
    public void cadastrarAgenda(@Payload Agenda agenda, @Header("simpSessionId") String sessionId) {
        agendaRepository.save(agenda);
    }
}
