package com.clinica.atendimento.whisper.service;

import com.clinica.atendimento.whisper.dto.WhisperResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WhisperService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whisper.api.url:http://192.168.122.153:5001/transcrever}")
    private String whisperApiUrl;

    public String transcrever(String sessao, byte[] audio) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audio, headers);
        try {
            var response = restTemplate.postForEntity(whisperApiUrl, requestEntity, WhisperResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody().getTranscricao();
            } else {
                return "[erro na transcrição: status " + response.getStatusCode() + "]";
            }
        } catch (Exception e) {
            return "[erro ao conectar ao whisper-api: " + e.getMessage() + "]";
        }
    }
}
