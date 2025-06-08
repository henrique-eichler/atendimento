package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.service.deepseek.DeepSeekService;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExtrairConsumer extends AbstractConsumer<String, String> {

    private final DeepSeekService deepSeekService;
    private final WebSocketHandler webSocketHandler;

    public ExtrairConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.extrair}") String topico,
            DeepSeekService deepSeekService,
            WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.deepSeekService = deepSeekService;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String texto = record.value();
        String extrato = deepSeekService.extrair(texto);

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/extrato", extrato);
    }
}
