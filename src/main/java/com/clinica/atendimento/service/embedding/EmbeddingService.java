package com.clinica.atendimento.service.embedding;

public interface EmbeddingService {
    float[] vectorize(String text);
}