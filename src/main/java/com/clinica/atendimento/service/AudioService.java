package com.clinica.atendimento.service;

import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AudioService {

    private final Map<String, List<Chunk>> sessoes = new ConcurrentHashMap<>();

    public void receberChunk(String sessao, Chunk chunk) {
        if (!sessoes.containsKey(sessao)) {
            sessoes.put(sessao, new ArrayList<>());
        }
        sessoes.get(sessao).add(chunk);
    }

    public byte[] finalizarSessao(String sessao) {
        List<Chunk> chunks = sessoes.get(sessao);
        sessoes.put(sessao, new ArrayList<>());

        return chunks != null
                ? chunks.stream()
                .sorted(Comparator.comparingInt(Chunk::getIndice))
                .map(Chunk::getAudio)
                .reduce(AudioService::join)
                .orElse(new byte[0])
                : new byte[0];
    }

    @PreDestroy
    public void cleanup() {
        sessoes.clear();
    }

    private static byte[] join(byte[] a, byte[] b) {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            byteArrayOutputStream.writeBytes(a);
            byteArrayOutputStream.writeBytes(b);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chunk {
        private int indice;
        private byte[] audio;
    }
}