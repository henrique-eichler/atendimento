package br.com.estimular.atendimento.service.transcrever;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranscreverService {

    @Getter
    @Setter
    private String service;
    private final List<ITranscreverService> services;

    public TranscreverService(
            @Value("${transcrever.service}") String service,
            List<ITranscreverService> services) {
        this.service = service;
        this.services = services;
    }

    public String transcrever(byte[] audio) {
        return services.stream()
                .filter(s -> s.getName().equals(service))
                .findFirst()
                .orElseThrow()
                .transcrever(audio);
    }
}