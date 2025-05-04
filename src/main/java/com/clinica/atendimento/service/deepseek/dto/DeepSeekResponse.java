package com.clinica.atendimento.service.deepseek.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeepSeekResponse {
    private String model;
    private String response;
}
