package com.clinica.atendimento.whisper.dto;

/**
 * Response object for the Whisper transcription service.
 * Contains the transcription text and the detected language.
 */
public class WhisperResponse {
    private String idioma;
    private String transcricao;

    public WhisperResponse() {
    }

    public WhisperResponse(String idioma, String transcricao) {
        this.idioma = idioma;
        this.transcricao = transcricao;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public String getTranscricao() {
        return transcricao;
    }

    public void setTranscricao(String transcricao) {
        this.transcricao = transcricao;
    }
}