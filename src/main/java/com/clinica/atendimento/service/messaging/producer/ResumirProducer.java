package com.clinica.atendimento.service.messaging.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResumirProducer extends AbstractProducer<String, String> {

    public ResumirProducer(@Value("${kafka.url}") String bootstrapServer,
                           @Value("${kafka.topico.resumir}") String topico) {
        super(bootstrapServer, topico, StringSerializer.class, StringSerializer.class);
    }
}