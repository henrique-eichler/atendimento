package com.clinica.atendimento.service.deepseek.dto

data class DeepSeekRequest(
    var model: String = "deepseek-llm:latest",
    var prompt: String? = null,
    var stream: Boolean = false
) {
    constructor(prompt: String) : this() {
        this.prompt = prompt
    }
}