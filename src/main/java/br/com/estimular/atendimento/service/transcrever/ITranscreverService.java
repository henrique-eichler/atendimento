package br.com.estimular.atendimento.service.transcrever;

public interface ITranscreverService {
    String getName();

    String transcrever(byte[] audio);
}
