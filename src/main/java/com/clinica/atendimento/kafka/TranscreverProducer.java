package com.clinica.atendimento.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class TranscreverProducer implements DisposableBean {

    private final Producer<String, byte[]> producer;
    private final String topico;

    public TranscreverProducer(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers, 
                              @Value("${app.kafka.topico.transcrever}") String topico) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class.getName());

        this.producer = new KafkaProducer<>(props);
        this.topico = topico;
    }

    public void enviarAudio(String sessao, byte[] audio) {
        producer.send(new ProducerRecord<>(topico, sessao, audio));
    }

    @Override
    public void destroy() {
        if (producer != null) {
            producer.close();
        }
    }
}
