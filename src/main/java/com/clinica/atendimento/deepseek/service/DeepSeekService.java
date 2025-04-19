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
        var texto = "Dado essa transcrição completa de uma sessão: \"" + prompt + "\". Responda: O que foi discutido durante a sessão?";
        var deepSeekRequest = new DeepSeekRequest(texto);

        try {
            var responseEntity = restTemplate.postForEntity(deepseekUrl, deepSeekRequest, DeepSeekResponse.class);
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
