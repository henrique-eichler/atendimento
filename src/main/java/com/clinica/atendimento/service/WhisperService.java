package com.clinica.atendimento.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

@Service
public class WhisperService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${whisper.api.url:http://192.168.122.153:5001/transcrever}")
    private String whisperApiUrl;

    public String transcrever(String sessao, byte[] audio) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try (FileOutputStream fileOutputStream = new FileOutputStream("/home/henrique/Downloads/" + sessao + ".wav")) {
            fileOutputStream.write(audio);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        HttpEntity<byte[]> requestEntity = new HttpEntity<>(audio, headers);
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(whisperApiUrl, requestEntity, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object transcricao = response.getBody().get("transcricao");
                return transcricao != null ? transcricao.toString() : "[sem conteúdo]";
            } else {
                return "[erro na transcrição: status " + response.getStatusCode() + "]";
            }
        } catch (Exception e) {
            return "[erro ao conectar ao whisper-api: " + e.getMessage() + "]";
        }
    }
}
