package com.clinica.atendimento.service.deepseek;

import com.clinica.atendimento.service.deepseek.dto.DeepSeekRequest;
import com.clinica.atendimento.service.deepseek.dto.DeepSeekResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class DeepSeekService {

    private final String url;
    private final String model;
    private final RestTemplate restTemplate;

    public DeepSeekService(
            @Value("${deepseek.url}") String url,
            @Value("${deepseek.model}") String model,
            RestTemplate restTemplate) {
        this.url = url;
        this.model = model;
        this.restTemplate = restTemplate;
    }

    public String resumir(String prompt) {
        var texto = """
                Dado essa transcrição completa de uma sessão:
                
                "%s"
                
                Responda: Faça um resumo da transcrição enumerando as atividades realizadas.
                """.formatted(prompt);
        var deepSeekRequest = new DeepSeekRequest(model, texto);

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

    public String extrair(String transcricao) {
        String prompt = """
                Você é um assistente de IA especialista em preencher formulários estruturados com base em transcrições de entrevistas clínicas.
                
                Leia a transcrição abaixo e preencha o seguinte JSON com as informações extraídas:
                
                {
                    "entrevistador": "",
                    "paciente": "",
                    "sintomas": [],
                    "diagnostico_presuntivo": "",
                    "recomendacoes": [],
                    "atestado": {
                       "dias": 0,
                       "cid": ""
                    },
                    "prescricao_medicamento": [
                        {
                            "medicamento": "",
                            "posologia": ""
                        }
                    ]
                }
                
                Transcrição:
                "%s"
                """.formatted(transcricao);
        var deepSeekRequest = new DeepSeekRequest(model, prompt);

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