package br.com.estimular.atendimento.service.messaging.producer;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranscreverProducer extends AbstractProducer<String, byte[]> {

    public TranscreverProducer(@Value("${kafka.url}") String bootstrapServer,
                               @Value("${kafka.topico.transcrever}") String topico) {
        super(bootstrapServer, topico, StringSerializer.class, ByteArraySerializer.class);
    }
}