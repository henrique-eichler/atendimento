package com.clinica.atendimento.service.consumer;

import com.clinica.atendimento.controller.WebSocketTranscricaoController;
import com.clinica.atendimento.service.deepseek.DeepSeekService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExtrairConsumer extends AbstractConsumer<String, String> {

    private final DeepSeekService deepSeekService;
    private final WebSocketTranscricaoController webSocketTranscricaoController;

    public ExtrairConsumer(DeepSeekService deepSeekService,
                           WebSocketTranscricaoController webSocketTranscricaoController,
                           @Value("${kafka.url}") String bootstrapServer,
                           @Value("${kafka.topico.extrair}") String topico) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.deepSeekService = deepSeekService;
        this.webSocketTranscricaoController = webSocketTranscricaoController;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String texto = record.value();
        String extrato = deepSeekService.extrair(texto);
        webSocketTranscricaoController.enviar(sessao, "extrato", extrato);
    }
}
