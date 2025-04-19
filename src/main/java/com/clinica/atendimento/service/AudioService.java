package com.clinica.atendimento.service;

import com.clinica.atendimento.kafka.TranscreverProducer;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AudioService {

    private final Map<String, List<Chunk>> sessoes = new ConcurrentHashMap<>();
    private final TranscreverProducer transcreverProducer;

    public AudioService(TranscreverProducer transcreverProducer) {
        this.transcreverProducer = transcreverProducer;
    }

    public void iniciarSessao(String sessao) {
        sessoes.put(sessao, new LinkedList<>());
    }

    public void receberChunk(String sessao, Chunk chunk) {
        sessoes.get(sessao).add(chunk);
    }

    public void finalizarSessao(String sessao) {
        sessoes.remove(sessao)
                .stream()
                .sorted((a, b) -> a.indice - b.indice)
                .map(Chunk::getAudio)
                .reduce(AudioService::join)
                .ifPresent(audio -> transcreverProducer.enviarAudio(sessao, audio));
        iniciarSessao(sessao);
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

    @PreDestroy
    public void cleanup() {
        sessoes.clear();
    }

    public static class Chunk {
        private int indice;
        private byte[] audio;

        public Chunk() {
        }

        public Chunk(int indice, byte[] audio) {
            this.indice = indice;
            this.audio = audio;
        }

        public int getIndice() {
            return indice;
        }

        public void setIndice(int indice) {
            this.indice = indice;
        }

        public byte[] getAudio() {
            return audio;
        }

        public void setAudio(byte[] audio) {
            this.audio = audio;
        }
    }
}
