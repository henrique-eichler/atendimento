package com.clinica.atendimento.service.producer

import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.Producer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.Serializer
import org.springframework.beans.factory.DisposableBean

import java.util.Properties

abstract class AbstractProducer<K, V>(
    bootstrapServer: String,
    private val topico: String,
    keySerializer: Class<out Serializer<K>>,
    valueSerializer: Class<out Serializer<V>>
) : DisposableBean {

    private val producer: Producer<K, V>

    init {
        val props = Properties()
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer.name)
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer.name)

        this.producer = KafkaProducer(props)
    }

    fun enviar(key: K, value: V) {
        producer.send(ProducerRecord(topico, key, value))
    }

    override fun destroy() {
        producer.close()
    }
}