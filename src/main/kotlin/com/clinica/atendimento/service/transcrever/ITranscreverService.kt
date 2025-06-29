package com.clinica.atendimento.service.transcrever

interface ITranscreverService {
    fun getName(): String

    fun transcrever(audio: ByteArray): String
}