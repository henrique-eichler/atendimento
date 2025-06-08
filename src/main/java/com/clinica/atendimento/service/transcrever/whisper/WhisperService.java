package com.clinica.atendimento.service.transcrever.whisper;

import com.clinica.atendimento.service.transcrever.ITranscreverService;
import com.clinica.atendimento.service.transcrever.whisper.dto.WhisperResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class WhisperService implements ITranscreverService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String whisperApiUrl;

    public WhisperService(@Value("${whisper.url}") String whisperApiUrl) {
        this.whisperApiUrl = whisperApiUrl;
    }

    @Override
    public String getName() {
        return "whisper";
    }

    public String transcrever(byte[] audio) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        try (FileOutputStream fileOutputStream = new FileOutputStream("/home/henrique/Downloads/audio.wav")) {
            fileOutputStream.write(audio);
        } catch (IOException ignored) {
        }

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
