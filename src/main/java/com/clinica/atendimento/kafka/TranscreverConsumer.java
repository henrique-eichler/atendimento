package com.clinica.atendimento.kafka;

import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import com.clinica.atendimento.whisper.service.WhisperService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TranscreverConsumer {

    private final WhisperService whisperService;
    private final ResumirProducer resumirProducer;
    private final ExtrairProducer extrairProducer;
    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;

    public TranscreverConsumer(WhisperService whisperService, ResumirProducer resumirProducer, ExtrairProducer extrairProducer, WebSocketTranscricaoHandler webSocketTranscricaoHandler) {
        this.whisperService = whisperService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
    }

    @KafkaListener(topics = "${app.kafka.topico.transcrever}", groupId = "transcricao-group", containerFactory = "byteArrayKafkaListenerContainerFactory")
    public void consumir(ConsumerRecord<String, byte[]> record) {
        String sessao = record.key();
        byte[] audio = record.value();
        String transcricao = whisperService.transcrever(sessao, audio);
        resumirProducer.enviar(sessao, transcricao);
        extrairProducer.enviar(sessao, transcricao);
        webSocketTranscricaoHandler.enviar(sessao, "transcricao", transcricao);
    }
}