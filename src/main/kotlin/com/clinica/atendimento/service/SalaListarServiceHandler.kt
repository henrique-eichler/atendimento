package com.clinica.atendimento.service

import com.clinica.atendimento.dto.SalaDTO
import com.clinica.atendimento.repository.SalaRepository
import com.clinica.atendimento.service.websocket.WebSocketHandler
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.socket.WebSocketSession
import java.io.IOException

@Service
class SalaListarServiceHandler(
    private val webSocketHandler: WebSocketHandler,
    private val salaRepository: SalaRepository
) : AbstractServiceHandler<Void>(Void::class.java) {

    init {
        webSocketHandler.register("/topic/sala/request/list", this)
    }

    @Transactional(readOnly = true)
    @Throws(IOException::class)
    override fun process(session: WebSocketSession, t: Void) {
        val list = salaRepository.findAllSalas().map { SalaDTO.fromEntity(it) }
        webSocketHandler.sendToSession(session, "/topic/sala/response/list", list)
    }
}