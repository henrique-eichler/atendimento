package com.clinica.atendimento.service.rag.impl.qdrant;

import com.clinica.atendimento.service.rag.RAGService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QDrantSessionRAGServiceImpl implements RAGService {

    private final String url;
    private final RestTemplate restTemplate;

    public QDrantSessionRAGServiceImpl(
            @Value("${qdrant.session.url}") String url,
            RestTemplate restTemplate) {
        this.url = url;
        this.restTemplate = restTemplate;
    }

    @Override
    public void store(String collection, String id, float[] embeddings, String text, Map<String, String> metadata) {
        var payload = new HashMap<>(metadata);
        payload.put("text", text);

        var point = Map.of(
                "id", id,
                "vector", embeddings,
                "payload", payload
        );

        var request = Map.of("points", List.of(point));

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var entity = new HttpEntity<>(request, headers);
        var response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao armazenar vetor no Qdrant: " + response.getBody());
        }
    }
}