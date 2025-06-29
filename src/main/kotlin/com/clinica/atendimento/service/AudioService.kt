package com.clinica.atendimento.service

import jakarta.annotation.PreDestroy
import org.springframework.stereotype.Service

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

@Service
class AudioService {

    private val sessoes: MutableMap<String, MutableList<Chunk>> = ConcurrentHashMap()

    fun receberChunk(sessao: String, chunk: Chunk) {
        if (!sessoes.containsKey(sessao)) {
            sessoes[sessao] = ArrayList()
        }
        sessoes[sessao]?.add(chunk)
    }

    fun finalizarSessao(sessao: String): ByteArray {
        val chunks = sessoes[sessao]
        sessoes[sessao] = ArrayList()

        return chunks?.stream()
            ?.sorted(Comparator.comparingInt { it.indice })
            ?.map { it.audio }
            ?.reduce { a, b -> join(a, b) }
            ?.orElse(ByteArray(0))
            ?: ByteArray(0)
    }

    @PreDestroy
    fun cleanup() {
        sessoes.clear()
    }

    private fun join(a: ByteArray, b: ByteArray): ByteArray {
        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                byteArrayOutputStream.writeBytes(a)
                byteArrayOutputStream.writeBytes(b)
                return byteArrayOutputStream.toByteArray()
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    data class Chunk(
        val indice: Int,
        val audio: ByteArray
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Chunk

            if (indice != other.indice) return false
            if (!audio.contentEquals(other.audio)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = indice
            result = 31 * result + audio.contentHashCode()
            return result
        }
    }
}