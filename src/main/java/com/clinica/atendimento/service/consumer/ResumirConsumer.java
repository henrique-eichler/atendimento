package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import com.clinica.atendimento.service.deepseek.DeepSeekService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResumirConsumer extends AbstractConsumer<String, String> {

    private final DeepSeekService deepSeekService;
    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;

    public ResumirConsumer(DeepSeekService deepSeekService,
                           WebSocketTranscricaoHandler webSocketTranscricaoHandler,
                           @Value("${kafka.url}") String bootstrapServer,
                           @Value("${kafka.topico.resumir}") String topico) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.deepSeekService = deepSeekService;
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String texto = record.value();
        String resumir = deepSeekService.resumir(texto);
        webSocketTranscricaoHandler.enviar(sessao, "resumo", resumir);
    }
}
