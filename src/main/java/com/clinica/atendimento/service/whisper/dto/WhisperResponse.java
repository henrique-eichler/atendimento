package com.clinica.atendimento.service.whisper.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Response object for the Whisper transcription service.
 * Contains the transcription text and the detected language.
 */
@Getter
@Setter
@Builder
public class WhisperResponse {
    private String idioma;
    private String transcricao;

    public WhisperResponse() {
    }

    public WhisperResponse(String idioma, String transcricao) {
        this.idioma = idioma;
        this.transcricao = transcricao;
    }
}