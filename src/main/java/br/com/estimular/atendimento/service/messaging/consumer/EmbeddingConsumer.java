package br.com.estimular.atendimento.service.messaging.consumer;

import br.com.estimular.atendimento.service.embedding.EmbeddingService;
import br.com.estimular.atendimento.service.messaging.dto.StoreVectorDto;
import br.com.estimular.atendimento.service.messaging.producer.StoreVectorProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class EmbeddingConsumer extends AbstractConsumer<String, String> {

    private final EmbeddingService embeddingService;
    private final StoreVectorProducer storeVectorProducer;

    public EmbeddingConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.embedding}") String topico,
            EmbeddingService embeddingService,
            StoreVectorProducer storeVectorProducer) {
        super(bootstrapServer, topico, StringDeserializer.class, StringDeserializer.class);
        this.embeddingService = embeddingService;
        this.storeVectorProducer = storeVectorProducer;
    }

    @Override
    protected void receber(ConsumerRecord<String, String> record) {
        String sessao = record.key();
        String transcription = record.value();
        float[] vector = embeddingService.vectorize(transcription);
        StoreVectorDto storeVectorDto = StoreVectorDto.builder()
                .id(sessao)
                .embeddings(vector)
                .text(transcription)
                .build();
        storeVectorProducer.enviar(sessao, storeVectorDto);
    }
}
