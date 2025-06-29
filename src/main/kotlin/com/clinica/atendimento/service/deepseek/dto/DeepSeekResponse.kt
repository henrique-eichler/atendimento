package com.clinica.atendimento.service.deepseek.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeepSeekResponse(
    val model: String? = null,
    val response: String? = null
)