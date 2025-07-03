package com.clinica.atendimento.service.reasoning.impl.deepseek;

import com.clinica.atendimento.service.reasoning.ReasoningService;
import com.clinica.atendimento.service.reasoning.impl.deepseek.dto.DeepSeekRequest;
import com.clinica.atendimento.service.reasoning.impl.deepseek.dto.DeepSeekResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekReasoningServiceImpl implements ReasoningService {

    private final String url;
    private final String model;
    private final RestTemplate restTemplate;

    public DeepSeekReasoningServiceImpl(
            @Value("${deepseek.generate.url}") String url,
            @Value("${deepseek.generate.model}") String model,
            RestTemplate restTemplate) {
        this.url = url;
        this.model = model;
        this.restTemplate = restTemplate;
    }

    @Override
    public String reason(String text) {
        var deepSeekRequest = new DeepSeekRequest(model, text);

        try {
            var responseEntity = restTemplate.postForEntity(url, deepSeekRequest, DeepSeekResponse.class);
            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody().getResponse().trim();
            } else {
                return "DeepSeek returned " + responseEntity.getStatusCode();
            }
        } catch (Exception ex) {
            return "Erro ao chamar DeepSeek: " + ex.getMessage();
        }
    }
}