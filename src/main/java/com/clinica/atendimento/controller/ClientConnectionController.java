package com.clinica.atendimento.controller;

import com.clinica.atendimento.service.ClientConnectionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/connections")
public class ClientConnectionController {

    private final ClientConnectionService clientConnectionService;

    public ClientConnectionController(ClientConnectionService clientConnectionService) {
        this.clientConnectionService = clientConnectionService;
    }

    /**
     * Get information about all connected clients
     * @return List of ClientInfo objects
     */
    @GetMapping
    public List<ClientConnectionService.ClientInfo> getConnectedClients() {
        return clientConnectionService.getConnectedClients();
    }
}