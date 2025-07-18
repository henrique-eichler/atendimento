package br.com.estimular.atendimento.service.reasoning.impl.deepseek.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeepSeekRequest {
    private String model;
    private String prompt;
    private boolean stream;

    public DeepSeekRequest(String model) {
        this.model = model;
        stream = false;
    }

    public DeepSeekRequest(String model, String prompt) {
        this(model);
        this.prompt = prompt;
    }
}
