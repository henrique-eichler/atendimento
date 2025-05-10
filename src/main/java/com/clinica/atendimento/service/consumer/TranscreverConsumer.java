package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import com.clinica.atendimento.service.producer.ExtrairProducer;
import com.clinica.atendimento.service.producer.ResumirProducer;
import com.clinica.atendimento.service.whisper.WhisperService;
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
    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;

    public TranscreverConsumer(@Value("${kafka.url}") String bootstrapServer,
                               @Value("${kafka.topico.transcrever}") String topico,
                               WhisperService whisperService,
                               ResumirProducer resumirProducer,
                               ExtrairProducer extrairProducer,
                               WebSocketTranscricaoHandler webSocketTranscricaoHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, ByteArrayDeserializer.class);
        this.whisperService = whisperService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, byte[]> record) {
        String sessao = record.key();
        byte[] audio = record.value();

        String transcricao = whisperService.transcrever(audio);
        resumirProducer.enviar(sessao, transcricao);
        extrairProducer.enviar(sessao, transcricao);
        webSocketTranscricaoHandler.enviar(sessao, "transcricao", transcricao);
    }
}