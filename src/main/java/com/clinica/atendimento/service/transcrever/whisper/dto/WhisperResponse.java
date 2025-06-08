package com.clinica.atendimento.service.transcrever.whisper.dto;

import lombok.*;

/**
 * Response object for the Whisper transcription service.
 * Contains the transcription text and the detected language.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WhisperResponse {
    private String idioma;
    private String transcricao;
}