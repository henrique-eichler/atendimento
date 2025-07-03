package com.clinica.atendimento.service.messaging.consumer;

import com.clinica.atendimento.service.reasoning.ReasoningService;
import com.clinica.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExtrairConsumer extends AbstractConsumer<String, String> {

    private final ReasoningService reasoningService;
    private final WebSocketHandler webSocketHandler;

    public ExtrairConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.extrair}") String topico,
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
                Você é um assistente de IA especialista em preencher formulários estruturados com base em transcrições de entrevistas clínicas.
                
                Leia a transcrição abaixo e preencha o seguinte JSON com as informações extraídas:
                
                {
                    "entrevistador": "",
                    "paciente": "",
                    "sintomas": [],
                    "diagnostico_presuntivo": "",
                    "recomendacoes": [],
                    "atestado": {
                       "dias": 0,
                       "cid": ""
                    },
                    "prescricao_medicamento": [
                        {
                            "medicamento": "",
                            "posologia": ""
                        }
                    ]
                }
                
                Transcrição:
                "%s"
                """.formatted(record.value());
        String extrato = reasoningService.reason(prompt);

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/extrato", extrato);
    }
}
