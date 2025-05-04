package com.clinica.atendimento.kafka;

import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import com.clinica.atendimento.service.whisper.WhisperService;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class TranscreverConsumer implements InitializingBean, DisposableBean {

    private final WhisperService whisperService;
    private final ResumirProducer resumirProducer;
    private final ExtrairProducer extrairProducer;
    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;
    private final String bootstrapServers;
    private final String topico;
    private final AtomicBoolean running = new AtomicBoolean(true);
    private Consumer<String, byte[]> consumer;
    private ExecutorService executorService;

    public TranscreverConsumer(WhisperService whisperService, 
                              ResumirProducer resumirProducer, 
                              ExtrairProducer extrairProducer, 
                              WebSocketTranscricaoHandler webSocketTranscricaoHandler,
                              @Value("${kafka.url}") String bootstrapServers,
                              @Value("${kafka.topico.transcrever}") String topico) {
        this.whisperService = whisperService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
        this.bootstrapServers = bootstrapServers;
        this.topico = topico;
    }

    @Override
    public void afterPropertiesSet() {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "transcricao-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ByteArrayDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList(topico));

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(this::consumirMensagens);
    }

    private void consumirMensagens() {
        try {
            while (running.get()) {
                ConsumerRecords<String, byte[]> records = consumer.poll(Duration.ofMillis(100));
                records.forEach(record -> {
                    String sessao = record.key();
                    byte[] audio = record.value();
                    String transcricao = whisperService.transcrever(audio);
                    resumirProducer.enviar(sessao, transcricao);
                    extrairProducer.enviar(sessao, transcricao);
                    webSocketTranscricaoHandler.enviar(sessao, "transcricao", transcricao);
                });
            }
        } finally {
            consumer.close();
        }
    }

    @Override
    public void destroy() {
        running.set(false);
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}
