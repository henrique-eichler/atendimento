package br.com.estimular.atendimento.service.messaging.consumer;

import br.com.estimular.atendimento.service.reasoning.ReasoningService;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResumirConsumer extends AbstractConsumer<String, String> {

    private final ReasoningService reasoningService;
    private final WebSocketHandler webSocketHandler;

    public ResumirConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.resumir}") String topico,
            ReasoningService reasoningService,
            WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.reasoningService = reasoningService;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        var sessao = record.key();
        var prompt = """
                Dado essa transcrição completa de uma sessão:
                
                "%s"
                
                Responda: Faça um resumo da transcrição enumerando as atividades realizadas.
                """.formatted(record.value());

        String resumo = reasoningService.reason(prompt);

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/resumo", resumo);
    }
}
