package com.clinica.atendimento.service;

import com.clinica.atendimento.websocket.WebSocketHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Base64;

@Service
public class TranscricaoChunkServiceHandler extends AbstractServiceHandler<TranscricaoChunkServiceHandler.Chunk> {

    private final WebSocketHandler webSocketHandler;
    private final AudioService audioService;

    public TranscricaoChunkServiceHandler(WebSocketHandler webSocketHandler, AudioService audioService) {
        super(Chunk.class);
        this.webSocketHandler = webSocketHandler;
        this.audioService = audioService;

        this.webSocketHandler.register("/topic/transcricao/chunk", this);
    }

    @Override
    public void process(WebSocketSession session, Chunk chunk) throws IOException {
        byte[] audio = Base64.getDecoder().decode(chunk.base64());
        AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.indice(), audio);
        audioService.receberChunk(this.webSocketHandler.getClientUuid(session), audioChunk);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    @Builder
    public static class Chunk {
        @JsonProperty
        private int indice;
        @JsonProperty
        private String base64;
    }
}
