package br.com.estimular.atendimento.serialization;

import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class FloatArraySerializer implements Serializer<float[]> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, float[] data) {
        if (data == null) return null;

        ByteBuffer buffer = ByteBuffer.allocate(4 + data.length * 4);
        buffer.putInt(data.length); // Store length first

        for (float f : data) {
            buffer.putFloat(f);
        }

        return buffer.array();
    }

    @Override
    public void close() {
    }
}
