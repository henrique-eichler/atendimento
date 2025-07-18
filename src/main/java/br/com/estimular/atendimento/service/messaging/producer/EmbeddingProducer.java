package br.com.estimular.atendimento.service.messaging.producer;

import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingProducer extends AbstractProducer<String, String> {

    public EmbeddingProducer(@Value("${kafka.url}") String bootstrapServer,
                             @Value("${kafka.topico.embedding}") String topico) {
        super(bootstrapServer, topico, StringSerializer.class, StringSerializer.class);
    }
}

