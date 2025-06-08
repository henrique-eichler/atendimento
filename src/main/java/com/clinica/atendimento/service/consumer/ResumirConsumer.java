package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.service.deepseek.DeepSeekService;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResumirConsumer extends AbstractConsumer<String, String> {

    private final DeepSeekService deepSeekService;
    private final WebSocketHandler webSocketHandler;

    public ResumirConsumer(DeepSeekService deepSeekService,
                           @Value("${kafka.url}") String bootstrapServer,
                           @Value("${kafka.topico.resumir}") String topico,
                           WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.deepSeekService = deepSeekService;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String texto = record.value();
        String resumo = deepSeekService.resumir(texto);

        System.out.println("resumo: " + resumo);

        TranscreverConsumer.Response response = new TranscreverConsumer.Response("resumo", resumo);
        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/resultado", response);
    }
}
