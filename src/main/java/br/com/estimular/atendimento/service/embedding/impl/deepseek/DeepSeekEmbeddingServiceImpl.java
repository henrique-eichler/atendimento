package br.com.estimular.atendimento.service.embedding.impl.deepseek;

import br.com.estimular.atendimento.service.embedding.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class DeepSeekEmbeddingServiceImpl implements EmbeddingService {

    private final String url;
    private final String model;
    private final RestTemplate restTemplate;

    public DeepSeekEmbeddingServiceImpl(
            @Value("${deepseek.embedding.url}") String url,
            @Value("${deepseek.embedding.model}") String model,
            RestTemplate restTemplate) {
        this.url = url;
        this.model = model;
        this.restTemplate = restTemplate;
    }

    @Override
    public float[] vectorize(String text) {
        Map<String, Object> request = Map.of(
                "model", model,
                "prompt", text
        );

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<>(request, headers);
        var response = restTemplate.postForEntity(url, entity, Map.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("Erro ao gerar vetores com Ollama");
        }

        Map<?, ?> body = response.getBody();
        Map<?, ?> embeddingObj = (Map<?, ?>) body.get("embedding");
        List<?> values = (List<?>) embeddingObj.get("values");

        float[] vetor = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            vetor[i] = ((Number) values.get(i)).floatValue();
        }

        return vetor;
    }
}
