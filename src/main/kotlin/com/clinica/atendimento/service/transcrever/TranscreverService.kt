package com.clinica.atendimento.service.transcrever

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class TranscreverService(
    @Value("\${transcrever.service}") var service: String,
    private val services: List<ITranscreverService>
) {
    fun transcrever(audio: ByteArray): String {
        return services.stream()
            .filter { it.getName() == service }
            .findFirst()
            .orElseThrow()
            .transcrever(audio)
    }
}