package com.clinica.atendimento.service.deepseek.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeepSeekRequest {
    private String model;
    private String prompt;
    private boolean stream;

    public DeepSeekRequest() {
        model = "deepseek-llm:latest";
        stream = false;
    }

    public DeepSeekRequest(String prompt) {
        this();
        this.prompt = prompt;
    }

    public DeepSeekRequest(String model, String prompt, boolean stream) {
        this.model = model;
        this.prompt = prompt;
        this.stream = stream;
    }
}
