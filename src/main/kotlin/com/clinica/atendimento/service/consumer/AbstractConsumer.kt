package com.clinica.atendimento.service.consumer

import org.apache.kafka.clients.consumer.Consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.Deserializer
import org.springframework.beans.factory.DisposableBean

import java.time.Duration
import java.util.Collections
import java.util.Properties
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean

abstract class AbstractConsumer<K, V>(
    bootstrapServer: String,
    topico: String,
    keyClassDeserializer: Class<out Deserializer<K>>,
    valueClassDeserializer: Class<out Deserializer<V>>
) : DisposableBean {

    private val running = AtomicBoolean(true)
    private val consumer: Consumer<K, V>
    private val executorService: ExecutorService

    init {
        val props = Properties()
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer)
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "resumo-group")
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyClassDeserializer.name)
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueClassDeserializer.name)
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

        consumer = KafkaConsumer(props)
        consumer.subscribe(Collections.singletonList(topico))

        executorService = Executors.newSingleThreadExecutor()
        executorService.submit { listem() }
    }

    private fun listem() {
        try {
            while (running.get()) {
                consumer
                    .poll(Duration.ofMillis(100))
                    .forEach { this.receber(it) }
            }
        } finally {
            consumer.close()
        }
    }

    protected abstract fun receber(record: ConsumerRecord<K, V>)

    override fun destroy() {
        running.set(false)
        executorService.shutdown()
    }
}