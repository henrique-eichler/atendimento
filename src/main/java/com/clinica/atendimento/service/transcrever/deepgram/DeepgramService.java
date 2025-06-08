package com.clinica.atendimento.service.transcrever.deepgram;

import com.clinica.atendimento.service.transcrever.ITranscreverService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class DeepgramService implements ITranscreverService {

    private final RestTemplate restTemplate = new RestTemplate();

    private final String deepgramApiUrl;
    private final String deepgramApiKey;

    public DeepgramService(
            @Value("${deepgram.api.url}") String deepgramApiUrl,
            @Value("${deepgram.api.key}") String deepgramApiKey) {
        this.deepgramApiUrl = deepgramApiUrl;
        this.deepgramApiKey = deepgramApiKey;
    }

    @Override
    public String getName() {
        return "deepgram";
    }

    public String transcrever(byte[] audio) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setBearerAuth(deepgramApiKey);

        HttpEntity<byte[]> request = new HttpEntity<>(audio, headers);

        String uri = UriComponentsBuilder.fromHttpUrl(deepgramApiUrl)
                .queryParam("diarize", "true")
                .queryParam("punctuate", "true")
                .queryParam("language", "pt")
                .build()
                .toUriString();

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(uri, request, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> results = (Map<String, Object>) response.getBody().get("results");
                if (results != null && results.containsKey("channels")) {
                    var channels = (java.util.List<Map<String, Object>>) results.get("channels");
                    if (!channels.isEmpty()) {
                        var alternatives = (Map<String, Object>) ((Map) channels.get(0)).get("alternatives");
                        return (String) ((Map) alternatives).get("transcript");
                    }
                }
                return "[empty transcript]";
            } else {
                return "[error: status " + response.getStatusCode() + "]";
            }
        } catch (Exception e) {
            return "[error: " + e.getMessage() + "]";
        }
    }
}
