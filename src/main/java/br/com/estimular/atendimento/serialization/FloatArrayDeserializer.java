package br.com.estimular.atendimento.serialization;

import org.apache.kafka.common.serialization.Deserializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class FloatArrayDeserializer implements Deserializer<float[]> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public float[] deserialize(String topic, byte[] data) {
        if (data == null || data.length < 4) return null;

        ByteBuffer buffer = ByteBuffer.wrap(data);
        int length = buffer.getInt(); // Read length

        float[] result = new float[length];
        for (int i = 0; i < length; i++) {
            result[i] = buffer.getFloat();
        }

        return result;
    }

    @Override
    public void close() {
    }
}
