package com.clinica.atendimento.service.producer

import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ResumirProducer(
    @Value("\${kafka.url}") bootstrapServer: String,
    @Value("\${kafka.topico.resumir}") topico: String
) : AbstractProducer<String, String>(
    bootstrapServer,
    topico,
    StringSerializer::class.java,
    StringSerializer::class.java
)