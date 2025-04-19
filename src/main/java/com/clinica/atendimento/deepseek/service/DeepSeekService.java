package com.clinica.atendimento.deepseek.service;

import com.clinica.atendimento.deepseek.dto.DeepSeekRequest;
import com.clinica.atendimento.deepseek.dto.DeepSeekResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekService {

    private final RestTemplate restTemplate;

    @Value("${deepseek.url}")
    private String deepseekUrl;

    public DeepSeekService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String interpretar(String prompt) {
        var req = new DeepSeekRequest("deepseek-coder:6.7b-instruct", prompt, false);

        try {
            var responseEntity = restTemplate.postForEntity(deepseekUrl, req, DeepSeekResponse.class);

            if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                return responseEntity.getBody().getResponse().trim();
            } else {
                throw new IllegalStateException("DeepSeek returned " + responseEntity.getStatusCode());
            }
        } catch (Exception ex) {
            throw new RuntimeException("Erro ao chamar DeepSeek", ex);
        }
    }
}
