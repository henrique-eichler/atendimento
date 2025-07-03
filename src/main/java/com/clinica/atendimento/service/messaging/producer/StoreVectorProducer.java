package com.clinica.atendimento.service.messaging.producer;

import com.clinica.atendimento.serialization.DtoSerializer;
import com.clinica.atendimento.service.messaging.dto.StoreVectorDto;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class StoreVectorProducer extends AbstractProducer<String, StoreVectorDto> {

    public StoreVectorProducer(
            @Value("${kafka.url}") String bootstrapServer,
            @Value("${kafka.topico.store}") String topico) {
        super(bootstrapServer, topico, StringSerializer.class, StoreVectorDtoSerializer.class);
    }

    public static class StoreVectorDtoSerializer extends DtoSerializer<StoreVectorDto> {
    }
}

