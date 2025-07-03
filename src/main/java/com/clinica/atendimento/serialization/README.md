# DTO Serialization and Deserialization

This package provides generic classes for serializing and deserializing DTO objects to and from JSON format. These classes are designed to be used with Kafka producers and consumers, but can also be used in other contexts where serialization/deserialization is needed.

## Classes

- `DtoSerializer<T>`: A generic serializer for DTO objects that implements Kafka's `Serializer` interface.
- `DtoDeserializer<T>`: A generic deserializer for DTO objects that implements Kafka's `Deserializer` interface.
- `DtoSerializationUtil`: A utility class that provides factory methods for creating serializers and deserializers.

## Usage

### In a Kafka Producer

```java
import com.clinica.atendimento.dto.PacienteDTO;
import com.clinica.atendimento.serialization.DtoSerializationUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ExampleProducer {
    public static void main(String[] args) {
        // Configure the producer
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, DtoSerializer.class.getName());

        // Create the producer
        Producer<String, PacienteDTO> producer = new KafkaProducer<>(props, new StringSerializer(), DtoSerializationUtil.createSerializer());

        // Create a DTO
        PacienteDTO pacienteDTO = new PacienteDTO(/* ... */);

        // Send the DTO
        producer.send(new ProducerRecord<>("pacientes", "key", pacienteDTO));

        // Close the producer
        producer.close();
    }
}
```

### In a Kafka Consumer

```java
import com.clinica.atendimento.dto.PacienteDTO;
import com.clinica.atendimento.serialization.DtoSerializationUtil;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ExampleConsumer {
    public static void main(String[] args) {
        // Configure the consumer
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "pacientes-group");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DtoDeserializer.class.getName());

        // Create the consumer
        Consumer<String, PacienteDTO> consumer = new KafkaConsumer<>(props, new StringDeserializer(), DtoSerializationUtil.createDeserializer(PacienteDTO.class));

        // Subscribe to the topic
        consumer.subscribe(Collections.singletonList("pacientes"));

        // Poll for records
        consumer.poll(Duration.ofMillis(100)).forEach(record -> {
            String key = record.key();
            PacienteDTO pacienteDTO = record.value();
            // Process the DTO
        });

        // Close the consumer
        consumer.close();
    }
}
```

### Using with AbstractProducer and AbstractConsumer

```java
import com.clinica.atendimento.dto.PacienteDTO;
import com.clinica.atendimento.service.messaging.producer.AbstractProducer;
import com.clinica.atendimento.serialization.DtoSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.stereotype.Component;

@Component
public class PacienteProducer extends AbstractProducer<String, PacienteDTO> {
    public PacienteProducer(String bootstrapServer, String topico) {
        super(bootstrapServer, topico, StringSerializer.class, DtoSerializer.class);
    }
}
```

```java
import com.clinica.atendimento.dto.PacienteDTO;
import com.clinica.atendimento.service.messaging.consumer.AbstractConsumer;
import com.clinica.atendimento.serialization.DtoDeserializer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

@Component
public class PacienteConsumer extends AbstractConsumer<String, PacienteDTO> {
    public PacienteConsumer(String bootstrapServer, String topico) {
        super(bootstrapServer, topico, StringDeserializer.class, DtoDeserializer.class);
    }

    @Override
    protected void receber(ConsumerRecord<String, PacienteDTO> record) {
        String key = record.key();
        PacienteDTO pacienteDTO = record.value();
        // Process the DTO
    }
}
```

## Notes

- The serializer and deserializer use Jackson for JSON serialization/deserialization.
- The deserializer requires the target DTO class type to be provided in the constructor.
- The serializer and deserializer include proper error handling and logging.