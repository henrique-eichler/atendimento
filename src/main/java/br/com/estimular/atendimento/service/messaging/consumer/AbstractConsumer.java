package br.com.estimular.atendimento.service.messaging.consumer;

import lombok.SneakyThrows;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.beans.factory.DisposableBean;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class AbstractConsumer<K, V> implements DisposableBean {

    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Consumer<K, V> consumer;
    private final ExecutorService executorService;

    public AbstractConsumer(
            String bootstrapServer,
            String topico,
            Class<? extends Deserializer<K>> keyClassDeserializer,
            Class<? extends Deserializer<V>> valueClassDeserializer) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "resumo-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyClassDeserializer.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueClassDeserializer.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topico));

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::listem);
    }

    @SneakyThrows
    private void listem() {
        try {
            while (running.get()) {
                consumer
                        .poll(Duration.ofMillis(100))
                        .forEach(this::receber);
            }
        } finally {
            consumer.close();
        }
    }

    protected abstract void receber(ConsumerRecord<K, V> record);

    @Override
    public void destroy() {
        running.set(false);
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
