package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.service.producer.ExtrairProducer;
import com.clinica.atendimento.service.producer.ResumirProducer;
import com.clinica.atendimento.service.whisper.WhisperService;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranscreverConsumer extends AbstractConsumer<String, byte[]> {

    private final WhisperService whisperService;
    private final ResumirProducer resumirProducer;
    private final ExtrairProducer extrairProducer;
    private final WebSocketHandler webSocketHandler;

    public TranscreverConsumer(@Value("${kafka.url}") String bootstrapServer,
                               @Value("${kafka.topico.transcrever}") String topico,
                               WhisperService whisperService,
                               ResumirProducer resumirProducer,
                               ExtrairProducer extrairProducer,
                               WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, ByteArrayDeserializer.class);
        this.whisperService = whisperService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, byte[]> record) {
        String sessao = record.key();
        byte[] audio = record.value();

        String transcrito = whisperService.transcrever(audio);
        resumirProducer.enviar(sessao, transcrito);
        extrairProducer.enviar(sessao, transcrito);

        Response response = new Response("transcrito", transcrito);
        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/resultado", response);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Accessors(fluent = true)
    @Builder
    public static class Response {
        @JsonProperty
        private String tipo;
        @JsonProperty
        private String conteudo;
    }
}
