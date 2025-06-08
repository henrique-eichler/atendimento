package com.clinica.atendimento.service.transcrever;

public interface ITranscreverService {
    String getName();

    String transcrever(byte[] audio);
}
