package com.clinica.atendimento.service.consumer

import com.clinica.atendimento.service.producer.ExtrairProducer
import com.clinica.atendimento.service.producer.ResumirProducer
import com.clinica.atendimento.service.transcrever.TranscreverService
import com.clinica.atendimento.service.websocket.WebSocketHandler
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.ByteArrayDeserializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.io.FileOutputStream
import java.io.IOException

@Component
class TranscreverConsumer(
    @Value("\${kafka.url}") bootstrapServer: String,
    @Value("\${kafka.topico.transcrever}") topico: String,
    private val transcreverService: TranscreverService,
    private val resumirProducer: ResumirProducer,
    private val extrairProducer: ExtrairProducer,
    private val webSocketHandler: WebSocketHandler
) : AbstractConsumer<String, ByteArray>(
    bootstrapServer,
    topico,
    StringDeserializer::class.java,
    ByteArrayDeserializer::class.java
) {

    override fun receber(record: ConsumerRecord<String, ByteArray>) {
        val sessao = record.key()
        val audio = record.value()

        try {
            FileOutputStream("/home/henrique/Downloads/$sessao.wav").use { fileOutputStream ->
                fileOutputStream.write(audio)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        val transcrito = transcreverService.transcrever(audio)
        resumirProducer.enviar(sessao, transcrito)
        extrairProducer.enviar(sessao, transcrito)

        webSocketHandler.sendToClientId(sessao, "/topic/transcricao/response/transcrito", transcrito)
    }
}