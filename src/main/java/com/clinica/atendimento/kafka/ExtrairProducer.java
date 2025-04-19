package com.clinica.atendimento.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ExtrairProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topico;

    public ExtrairProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${app.kafka.topico.extrair}") String topico) {
        this.kafkaTemplate = kafkaTemplate;
        this.topico = topico;
    }

    public void enviar(String sessao, String texto) {
        kafkaTemplate.send(new ProducerRecord<>(topico, sessao, texto));
    }
}