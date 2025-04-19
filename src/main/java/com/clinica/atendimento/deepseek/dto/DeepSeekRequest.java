package com.clinica.atendimento.deepseek.dto;


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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}
