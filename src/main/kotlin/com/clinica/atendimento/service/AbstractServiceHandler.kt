package com.clinica.atendimento.service

import org.springframework.web.socket.WebSocketSession
import java.io.IOException

abstract class AbstractServiceHandler<T>(
    val type: Class<T>
) {
    @Throws(IOException::class)
    abstract fun process(session: WebSocketSession, t: T)
}