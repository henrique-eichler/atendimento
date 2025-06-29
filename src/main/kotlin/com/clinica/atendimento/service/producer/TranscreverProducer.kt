package com.clinica.atendimento.service.producer

import org.apache.kafka.common.serialization.ByteArraySerializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TranscreverProducer(
    @Value("\${kafka.url}") bootstrapServer: String,
    @Value("\${kafka.topico.transcrever}") topico: String
) : AbstractProducer<String, ByteArray>(
    bootstrapServer,
    topico,
    StringSerializer::class.java,
    ByteArraySerializer::class.java
)