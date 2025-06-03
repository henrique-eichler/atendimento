package com.clinica.atendimento.service;

import com.clinica.atendimento.service.producer.TranscreverProducer;
import jakarta.annotation.PreDestroy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AudioService {

    private final Map<String, List<Chunk>> sessoes = new ConcurrentHashMap<>();
    private final TranscreverProducer transcreverProducer;

    public AudioService(TranscreverProducer transcreverProducer) {
        this.transcreverProducer = transcreverProducer;
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

    public void iniciarSessao(String sessao) {
        sessoes.put(sessao, new ArrayList<>());
    }

    public void receberChunk(String sessao, Chunk chunk) {
        Objects.requireNonNull(sessoes.putIfAbsent(sessao, new ArrayList<>()))
                .add(chunk);
    }

    public void finalizarSessao(String sessao) {
        List<Chunk> chunks = sessoes.get(sessao);
        sessoes.put(sessao, new ArrayList<>());

        chunks.stream()
                .sorted(Comparator.comparingInt(Chunk::getIndice))
                .map(Chunk::getAudio)
                .reduce(AudioService::join)
                .ifPresent(audio -> transcreverProducer.enviar(sessao, audio));
    }

    @PreDestroy
    public void cleanup() {
        sessoes.clear();
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chunk {
        private int indice;
        private byte[] audio;
    }
}