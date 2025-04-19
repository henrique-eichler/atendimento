package com.clinica.atendimento.kafka;

import com.clinica.atendimento.deepseek.service.DeepSeekService;
import com.clinica.atendimento.handler.WebSocketTranscricaoHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ExtrairConsumer {

    private final DeepSeekService deepSeekService;
    private final WebSocketTranscricaoHandler webSocketTranscricaoHandler;

    public ExtrairConsumer(DeepSeekService deepSeekService, WebSocketTranscricaoHandler webSocketTranscricaoHandler) {
        this.deepSeekService = deepSeekService;
        this.webSocketTranscricaoHandler = webSocketTranscricaoHandler;
    }

    @KafkaListener(topics = "resumir", groupId = "resumo-group", containerFactory = "stringKafkaListenerContainerFactory")
    public void consumir(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String texto = record.value();
        String extrato = deepSeekService.extrair(texto);
        webSocketTranscricaoHandler.enviar(sessao, "extrato", extrato);
    }
}
