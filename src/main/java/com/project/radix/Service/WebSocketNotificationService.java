package com.project.radix.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.radix.DTO.AlertResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class WebSocketNotificationService extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void broadcastAlert(AlertResponse alert) {
        try {
            String json = objectMapper.writeValueAsString(alert);
            TextMessage message = new TextMessage(json);
            sessions.forEach(session -> {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(message);
                    }
                } catch (Exception e) {
                    // Log and continue
                }
            });
        } catch (Exception e) {
            // Log error
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) {
        sessions.remove(session);
    }

    public int getActiveSessions() {
        return sessions.size();
    }
}