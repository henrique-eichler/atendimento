package br.com.estimular.atendimento.service.transcrever.deepgram;

import br.com.estimular.atendimento.service.transcrever.ITranscreverService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class DeepgramService implements ITranscreverService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        headers.add("Content-Type", "audio/wav");
        headers.add("Authorization", "Token " + deepgramApiKey);

        HttpEntity<byte[]> request = new HttpEntity<>(audio, headers);

        String uri = UriComponentsBuilder.fromHttpUrl(deepgramApiUrl)
                .queryParam("diarize", "true")
                .queryParam("punctuate", "true")
                .queryParam("smart_format", "true")
                .queryParam("language", "pt")
                .build()
                .toUriString();

        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, request, String.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            return "[erro na transcrição: status " + response.getStatusCode() + "]";
        }

        try {
            JsonNode transcriptNode = objectMapper.readTree(response.getBody())
                    .path("results")
                    .path("channels")
                    .path(0)
                    .path("alternatives")
                    .path(0)
                    .path("transcript");

            return transcriptNode.asText("");
        } catch (JsonProcessingException e) {
            return "[erro ao conectar ao whisper-api: " + e.getMessage() + "]";
        }
    }
}
