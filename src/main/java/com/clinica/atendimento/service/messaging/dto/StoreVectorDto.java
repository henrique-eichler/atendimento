package com.clinica.atendimento.service.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(fluent = true)
@Builder
public class StoreVectorDto {

    @JsonProperty
    private String id;
    @JsonProperty
    private float[] embeddings;
    @JsonProperty
    private String text;
    @JsonProperty
    private Map<String, String> metadata;

}
