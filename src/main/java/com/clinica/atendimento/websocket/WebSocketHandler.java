package com.clinica.atendimento.websocket;

import com.clinica.atendimento.dto.RecursoDTO;
import com.clinica.atendimento.dto.SalaDTO;
import com.clinica.atendimento.dto.TerapiaDTO;
import com.clinica.atendimento.model.Recurso;
import com.clinica.atendimento.model.RecursoSala;
import com.clinica.atendimento.model.Sala;
import com.clinica.atendimento.model.Terapia;
import com.clinica.atendimento.model.TerapiaSala;
import com.clinica.atendimento.repository.RecursoRepository;
import com.clinica.atendimento.repository.RecursoSalaRepository;
import com.clinica.atendimento.repository.SalaRepository;
import com.clinica.atendimento.repository.TerapiaRepository;
import com.clinica.atendimento.repository.TerapiaSalaRepository;
import com.clinica.atendimento.service.AudioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private final RecursoRepository recursoRepository;
    private final SalaRepository salaRepository;
    private final TerapiaSalaRepository terapiaSalaRepository;
    private final RecursoSalaRepository recursoSalaRepository;
    private final TerapiaRepository terapiaRepository;
    private final AudioService audioService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    /**
     * Get all active WebSocket sessions
     * @return Map of client IDs to WebSocketSession objects
     */
    public Map<String, WebSocketSession> getSessions() {
        return sessions;
    }

    public WebSocketHandler(RecursoRepository recursoRepository,
                           SalaRepository salaRepository,
                           TerapiaSalaRepository terapiaSalaRepository,
                           RecursoSalaRepository recursoSalaRepository,
                           TerapiaRepository terapiaRepository,
                           AudioService audioService,
                           ObjectMapper objectMapper) {
        this.recursoRepository = recursoRepository;
        this.salaRepository = salaRepository;
        this.terapiaSalaRepository = terapiaSalaRepository;
        this.recursoSalaRepository = recursoSalaRepository;
        this.terapiaRepository = terapiaRepository;
        this.audioService = audioService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        // Store the session with the client UUID as the key
        String clientUuid = getClientUuid(session);
        sessions.put(clientUuid, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        JsonNode jsonNode = objectMapper.readTree(payload);

        // Extract destination and body from the message
        String destination = jsonNode.get("destination").asText();
        JsonNode body = jsonNode.has("body") ? objectMapper.readTree(jsonNode.get("body").asText()) : null;

        // Handle different message types based on destination
        switch (destination) {
            // Recurso endpoints
            case "/app/recurso/listar":
                handleListarRecursos(session);
                break;
            case "/app/recurso/salvar":
                handleSalvarRecurso(session, body);
                break;
            case "/app/recurso/excluir":
                handleExcluirRecurso(session, body);
                break;

            // Sala endpoints
            case "/app/sala/listar":
                handleListarSalas(session);
                break;
            case "/app/sala/salvar":
                handleSalvarSala(session, body);
                break;
            case "/app/sala/excluir":
                handleExcluirSala(session, body);
                break;

            // Terapia endpoints
            case "/app/terapia/listar":
                handleListarTerapias(session);
                break;
            case "/app/terapia/salvar":
                handleSalvarTerapia(session, body);
                break;
            case "/app/terapia/excluir":
                handleExcluirTerapia(session, body);
                break;

            // Transcricao endpoints
            case "/app/transcricao/iniciar":
                handleIniciarTranscricao(session);
                break;
            case "/app/transcricao/chunk":
                handleChunkTranscricao(session, body);
                break;
            case "/app/transcricao/finalizar":
                handleFinalizarTranscricao(session);
                break;

            default:
                // Unknown destination
                break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        // Remove the session when the connection is closed
        String clientUuid = getClientUuid(session);
        sessions.remove(clientUuid);
    }

    private String getClientUuid(WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        return (String) attributes.getOrDefault("clientUuid", session.getId());
    }

    private void handleListarRecursos(WebSocketSession session) throws IOException {
        var todos = recursoRepository.findAll()
                .stream()
                .map(RecursoDTO::fromEntity)
                .collect(Collectors.toList());

        sendToSession(session, "/topic/recurso/retorno/listar", todos);
    }

    private void handleSalvarRecurso(WebSocketSession session, JsonNode body) throws IOException {
        RecursoDTO recursoDTO = objectMapper.treeToValue(body, RecursoDTO.class);
        boolean novo = recursoDTO.getId() == null;
        Recurso recurso = recursoDTO.toEntity();
        var salvo = recursoRepository.save(recurso);

        String destination = novo ? "/topic/recurso/retorno/salvar" : "/topic/recurso/retorno/editar";
        sendToSession(session, destination, RecursoDTO.fromEntity(salvo));
    }

    private void handleExcluirRecurso(WebSocketSession session, JsonNode body) throws IOException {
        Long id = body.asLong();
        recursoRepository.deleteById(id);

        sendToSession(session, "/topic/recurso/retorno/excluir", id);
    }

    private void sendToSession(WebSocketSession session, String destination, Object data) throws IOException {
        try {
            // Create a message with destination and body
            Map<String, Object> message = Map.of(
                    "destination", destination,
                    "body", data
            );

            // Convert to JSON and send
            String jsonMessage = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (JsonProcessingException e) {
            throw new IOException("Error serializing message", e);
        }
    }

    // Sala handlers
    protected void handleListarSalas(WebSocketSession session) throws IOException {
        var todos = salaRepository.findAllSalas()
                .stream()
                .map(SalaDTO::fromEntity)
                .collect(Collectors.toList());
        sendToSession(session, "/topic/sala/retorno/listar", todos);
    }

    @Transactional
    protected void handleSalvarSala(WebSocketSession session, JsonNode body) throws IOException {
        SalaDTO salaDTO = objectMapper.treeToValue(body, SalaDTO.class);
        boolean novo = salaDTO.getId() == null;
        Sala sala = salaDTO.toEntity();
        var salvo = salaRepository.save(sala);

        if (!novo) {
            terapiaSalaRepository.deleteBySala(salvo);
            recursoSalaRepository.deleteBySala(salvo);
        }

        if (salaDTO.getTerapias() != null) {
            salaDTO.getTerapias()
                    .stream()
                    .map(terapiaDTO -> TerapiaSala.builder().sala(salvo).terapia(terapiaDTO.toEntity()).build())
                    .forEach(terapiaSalaRepository::save);
        }

        if (salaDTO.getRecursos() != null) {
            salaDTO.getRecursos()
                    .stream()
                    .map(recursoDTO -> new RecursoSala().sala(salvo).recurso(recursoDTO.toEntity()))
                    .forEach(recursoSalaRepository::save);
        }

        Sala reloaded = salaRepository.findById(salvo.id()).orElse(salvo);
        String destination = novo ? "/topic/sala/retorno/salvar" : "/topic/sala/retorno/editar";
        sendToSession(session, destination, SalaDTO.fromEntity(reloaded));
    }

    @Transactional
    protected void handleExcluirSala(WebSocketSession session, JsonNode body) throws IOException {
        Long id = body.asLong();
        salaRepository.findById(id).ifPresent(sala -> {
            terapiaSalaRepository.deleteBySala(sala);
            recursoSalaRepository.deleteBySala(sala);
        });

        salaRepository.deleteById(id);
        sendToSession(session, "/topic/sala/retorno/excluir", id);
    }

    // Terapia handlers
    private void handleListarTerapias(WebSocketSession session) throws IOException {
        var todos = terapiaRepository.findAll()
                .stream()
                .map(TerapiaDTO::fromEntity)
                .collect(Collectors.toList());
        sendToSession(session, "/topic/terapia/retorno/listar", todos);
    }

    private void handleSalvarTerapia(WebSocketSession session, JsonNode body) throws IOException {
        TerapiaDTO terapiaDTO = objectMapper.treeToValue(body, TerapiaDTO.class);
        boolean novo = terapiaDTO.getId() == null;
        Terapia terapia = terapiaDTO.toEntity();
        var salvo = terapiaRepository.save(terapia);

        String destination = novo ? "/topic/terapia/retorno/salvar" : "/topic/terapia/retorno/editar";
        sendToSession(session, destination, TerapiaDTO.fromEntity(salvo));
    }

    private void handleExcluirTerapia(WebSocketSession session, JsonNode body) throws IOException {
        Long id = body.asLong();
        terapiaRepository.deleteById(id);
        sendToSession(session, "/topic/terapia/retorno/excluir", id);
    }

    // Transcricao handlers
    private void handleIniciarTranscricao(WebSocketSession session) {
        String clientId = getClientUuid(session);
        audioService.iniciarSessao(clientId);
    }

    private void handleChunkTranscricao(WebSocketSession session, JsonNode body) throws IOException {
        String clientId = getClientUuid(session);
        try {
            Chunk chunk = objectMapper.treeToValue(body, Chunk.class);
            byte[] audio = Base64.getDecoder().decode(chunk.getBase64());
            AudioService.Chunk audioChunk = new AudioService.Chunk(chunk.getIndice(), audio);
            audioService.receberChunk(clientId, audioChunk);
        } catch (IllegalArgumentException e) {
            // Handle base64 decoding error
            sendToSession(session, "/user/" + clientId + "/queue/transcricao/error", 
                    new Response("error", "Error decoding audio data"));
        }
    }

    private void handleFinalizarTranscricao(WebSocketSession session) {
        String clientId = getClientUuid(session);
        audioService.finalizarSessao(clientId);
    }

    // Method to be called by consumer services
    public void enviarTranscricao(String clientId, String tipo, String conteudo) throws IOException {
        Response response = new Response(tipo, conteudo);

        // Find the session for this client
        WebSocketSession session = sessions.get(clientId);
        if (session != null && session.isOpen()) {
            sendToSession(session, "/user/" + clientId + "/queue/transcricao/resultado", response);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String tipo;
        private String conteudo;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Chunk {
        private int indice;
        private String base64;
    }

    // Broadcast a message to all connected sessions
    public void broadcast(String destination, Object data) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                sendToSession(session, destination, data);
            }
        }
    }
}
