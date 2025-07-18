package br.com.estimular.atendimento.service.embedding;

public interface EmbeddingService {
    float[] vectorize(String text);
}