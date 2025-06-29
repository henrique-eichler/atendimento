package com.clinica.atendimento.service.deepseek

import com.clinica.atendimento.service.deepseek.dto.DeepSeekRequest
import com.clinica.atendimento.service.deepseek.dto.DeepSeekResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class DeepSeekService(private val restTemplate: RestTemplate) {

    @Value("\${deepseek.url}")
    private lateinit var deepseekUrl: String

    fun resumir(prompt: String): String {
        val texto = "Dado essa transcrição completa de uma sessão: \"$prompt\". Responda: O que foi discutido durante a sessão?"
        val deepSeekRequest = DeepSeekRequest(texto)

        return try {
            val responseEntity = restTemplate.postForEntity(deepseekUrl, deepSeekRequest, DeepSeekResponse::class.java)
            if (responseEntity.statusCode.is2xxSuccessful && responseEntity.body != null) {
                responseEntity.body!!.response?.trim() ?: ""
            } else {
                "DeepSeek returned ${responseEntity.statusCode}"
            }
        } catch (ex: Exception) {
            "Erro ao chamar DeepSeek: ${ex.message}"
        }
    }

    fun extrair(transcricao: String): String {
        val prompt = """
            Você é um assistente de IA especialista em preencher formulários estruturados com base em transcrições de entrevistas clínicas.
            
            Leia a transcrição abaixo e preencha o seguinte JSON com as informações extraídas:
            
            {
                "entrevistador": "",
                "paciente": "",
                "sintomas": [],
                "diagnostico_presuntivo": "",
                "recomendacoes": [],
                "atestado": {
                   "dias": 0,
                   "cid": ""
                },
                "prescricao_medicamento": [
                    {
                        "medicamento": "",
                        "posologia": ""
                    }
                ]
            }
            
            Transcrição:
            "$transcricao"
        """.trimIndent()
        val deepSeekRequest = DeepSeekRequest(prompt)

        return try {
            val responseEntity = restTemplate.postForEntity(deepseekUrl, deepSeekRequest, DeepSeekResponse::class.java)
            if (responseEntity.statusCode.is2xxSuccessful && responseEntity.body != null) {
                responseEntity.body!!.response?.trim() ?: ""
            } else {
                "DeepSeek returned ${responseEntity.statusCode}"
            }
        } catch (ex: Exception) {
            "Erro ao chamar DeepSeek: ${ex.message}"
        }
    }
}