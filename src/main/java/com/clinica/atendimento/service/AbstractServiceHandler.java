package com.clinica.atendimento.service;

import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Getter
public abstract class AbstractServiceHandler<T> {

    private final Class<T> type;

    public AbstractServiceHandler(Class<T> clazz) {
        this.type = clazz;
    }

    public abstract void process(WebSocketSession session, T t) throws IOException;

}
