package com.clinica.atendimento.service.consumer

import com.clinica.atendimento.service.deepseek.DeepSeekService
import com.clinica.atendimento.service.websocket.WebSocketHandler
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class ExtrairConsumer(
    @Value("\${kafka.url}") bootstrapServer: String,
    @Value("\${kafka.topico.extrair}") topico: String,
    private val deepSeekService: DeepSeekService,
    private val webSocketHandler: WebSocketHandler
) : AbstractConsumer<String, String>(
    bootstrapServer,
    topico,
    StringDeserializer::class.java,
    StringDeserializer::class.java
) {

    override fun receber(record: ConsumerRecord<String, String>) {
        val sessao = record.key()
        val texto = record.value()
        val extrato = deepSeekService.extrair(texto)

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/extrato", extrato)
    }
}