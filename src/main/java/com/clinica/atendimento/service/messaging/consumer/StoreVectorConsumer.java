package com.clinica.atendimento.service.messaging.consumer;

import com.clinica.atendimento.serialization.DtoDeserializer;
import com.clinica.atendimento.service.messaging.dto.StoreVectorDto;
import com.clinica.atendimento.service.rag.RAGService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StoreVectorConsumer extends AbstractConsumer<String, StoreVectorDto> {

    private final RAGService ragService;

    public StoreVectorConsumer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.store}") String topico,
            RAGService ragService) {
        super(bootstrapServer, topico, StringDeserializer.class, StoreVectorDtoDeserializer.class);
        this.ragService = ragService;
    }

    @Override
    protected void receber(ConsumerRecord<String, StoreVectorDto> record) {
        String sessao = record.key();
        StoreVectorDto storeVector = record.value();
        ragService.store(sessao, storeVector.id(), storeVector.embeddings(), storeVector.text(), storeVector.metadata());
    }

    public static class StoreVectorDtoDeserializer extends DtoDeserializer<StoreVectorDto> {
        public StoreVectorDtoDeserializer(Class<StoreVectorDto> targetType) {
            super(targetType);
        }
    }
}

