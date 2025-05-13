package com.clinica.atendimento.service.deepseek;

import com.clinica.atendimento.service.deepseek.dto.DeepSeekRequest;
import com.clinica.atendimento.service.deepseek.dto.DeepSeekResponse;
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

    public String resumir(String prompt) {
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
        var deepSeekRequest = new DeepSeekRequest(prompt);

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
