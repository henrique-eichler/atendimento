package com.clinica.atendimento.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TranscreverProducer {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;
    private final String topico;

    public TranscreverProducer(KafkaTemplate<String, byte[]> kafkaTemplate, @Value("${app.kafka.topico.transcrever}") String topico) {
        this.kafkaTemplate = kafkaTemplate;
        this.topico = topico;
    }

    public void enviarAudio(String sessao, byte[] audio) {
        kafkaTemplate.send(new ProducerRecord<>(topico, sessao, audio));
    }
}