package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.service.producer.ExtrairProducer;
import com.clinica.atendimento.service.producer.ResumirProducer;
import com.clinica.atendimento.service.transcrever.TranscreverService;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class TranscreverConsumer extends AbstractConsumer<String, byte[]> {

    private final TranscreverService transcreverService;
    private final ResumirProducer resumirProducer;
    private final ExtrairProducer extrairProducer;
    private final WebSocketHandler webSocketHandler;

    public TranscreverConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.transcrever}") String topico,
            TranscreverService transcreverService,
            ResumirProducer resumirProducer,
            ExtrairProducer extrairProducer,
            WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, ByteArrayDeserializer.class);
        this.transcreverService = transcreverService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, byte[]> record) {
        String sessao = record.key();
        byte[] audio = record.value();

        try (FileOutputStream fileOutputStream = new FileOutputStream("/home/henrique/Downloads/" + sessao + ".wav")) {
            fileOutputStream.write(audio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String transcrito = transcreverService.transcrever(audio);
        resumirProducer.enviar(sessao, transcrito);
        extrairProducer.enviar(sessao, transcrito);

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/transcrito", transcrito);
    }
}
