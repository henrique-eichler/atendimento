package com.clinica.atendimento.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Bean(name = "byteArrayConsumerFactory")
    public ConsumerFactory<String, byte[]> byteArrayConsumerFactory() {
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "transcricao-group");

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "stringConsumerFactory")
    public ConsumerFactory<String, String> stringConsumerFactory() {
        var properties = new HashMap<String, Object>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "resumo-group");

        return new DefaultKafkaConsumerFactory<>(properties);
    }

    @Bean(name = "byteArrayKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, byte[]> byteArrayKafkaListenerContainerFactory(ConsumerFactory<String, byte[]> byteArrayConsumerFactory) {
        var concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<String, byte[]>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(byteArrayConsumerFactory);
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ProducerFactory<String, byte[]> byteArrayProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ByteArraySerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, byte[]> byteArrayKafkaTemplate(ProducerFactory<String, byte[]> byteArrayProducerFactory) {
        return new KafkaTemplate<>(byteArrayProducerFactory);
    }


    @Bean(name = "stringKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> stringKafkaListenerContainerFactory(ConsumerFactory<String, String> stringConsumerFactory) {
        var concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        concurrentKafkaListenerContainerFactory.setConsumerFactory(stringConsumerFactory);
        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    public ProducerFactory<String, String> stringProducerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        config.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(config);
    }

    @Bean
    public KafkaTemplate<String, String> stringKafkaTemplate(ProducerFactory<String, String> stringProducerFactory) {
        return new KafkaTemplate<>(stringProducerFactory);
    }
}