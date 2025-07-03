package com.clinica.atendimento.service.messaging.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.Serializer;
import org.springframework.beans.factory.DisposableBean;

import java.util.Properties;

public abstract class AbstractProducer<K, V> implements DisposableBean {

    private final Producer<K, V> producer;
    private final String topico;

    public AbstractProducer(
            String bootstrapServer,
            String topico,
            Class<? extends Serializer<K>> keySerializer,
            Class<? extends Serializer<V>> valueSerializer) {

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.getName());

        this.producer = new KafkaProducer<>(props);
        this.topico = topico;
    }

    public void enviar(K key, V value) {
        producer.send(new ProducerRecord<>(topico, key, value));
    }

    @Override
    public void destroy() {
        if (producer != null) {
            producer.close();
        }
    }
}