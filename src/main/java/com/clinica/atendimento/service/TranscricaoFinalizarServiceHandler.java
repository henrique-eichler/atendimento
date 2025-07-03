package com.clinica.atendimento.service;

import com.clinica.atendimento.service.messaging.producer.TranscreverProducer;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Service
public class TranscricaoFinalizarServiceHandler extends AbstractServiceHandler<Void> {

    private final WebSocketHandler webSocketHandler;
    private final AudioService audioService;
    private final TranscreverProducer transcreverProducer;

    public TranscricaoFinalizarServiceHandler(WebSocketHandler webSocketHandler, AudioService audioService, TranscreverProducer transcreverProducer) {
        super(Void.class);
        this.webSocketHandler = webSocketHandler;
        this.audioService = audioService;
        this.transcreverProducer = transcreverProducer;

        this.webSocketHandler.register("/topic/transcricao/finalizar", this);
    }

    @Override
    public void process(WebSocketSession session, Void unused) throws IOException {
        final String sessao = this.webSocketHandler.getClientUuid(session);
        final byte[] audio = audioService.finalizarSessao(sessao);
        transcreverProducer.enviar(sessao, audio);
    }
}