package com.clinica.atendimento.deepseek.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class DeepSeekRequest {
    private String model;
    private String prompt;
    private boolean stream;
}
