package com.clinica.atendimento.service.rag;

import java.util.Map;

public interface RAGService {
    void store(String collection, String id, float[] embeddings, String text, Map<String, String> metadata);
}
