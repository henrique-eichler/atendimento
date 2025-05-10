package com.clinica.atendimento.service.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExtrairProducer extends AbstractProducer<String, String> {

    public ExtrairProducer(@Value("${kafka.url}") String bootstrapServer,
                           @Value("${kafka.topico.extrair}") String topico) {
        super(bootstrapServer, topico, StringSerializer.class, StringSerializer.class);
    }
}
