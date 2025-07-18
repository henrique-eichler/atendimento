package br.com.estimular.atendimento.service.messaging.consumer;

import br.com.estimular.atendimento.service.messaging.producer.ExtrairProducer;
import br.com.estimular.atendimento.service.messaging.producer.ResumirProducer;
import br.com.estimular.atendimento.service.transcrever.TranscreverService;
import br.com.estimular.atendimento.service.websocket.WebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.ByteArrayDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TranscreverConsumer extends AbstractConsumer<String, byte[]> {

    private final TranscreverService transcreverService;
    private final ResumirProducer resumirProducer;
    private final ExtrairProducer extrairProducer;
    private final WebSocketHandler webSocketHandler;

    public TranscreverConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.transcrever}") String topico,
            TranscreverService transcreverService,
            ResumirProducer resumirProducer,
            ExtrairProducer extrairProducer,
            WebSocketHandler webSocketHandler) {
        super(bootstrapServer, topico, StringDeserializer.class, ByteArrayDeserializer.class);
        this.transcreverService = transcreverService;
        this.resumirProducer = resumirProducer;
        this.extrairProducer = extrairProducer;
        this.webSocketHandler = webSocketHandler;
    }

    @Override
    protected void receber(ConsumerRecord<String, byte[]> record) {
        String sessao = record.key();
        byte[] audio = record.value();

        String transcrito = transcreverService.transcrever(audio);
        resumirProducer.enviar(sessao, transcrito);
        extrairProducer.enviar(sessao, transcrito);

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/transcrito", transcrito);
    }
}
