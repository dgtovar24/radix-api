package com.project.radix.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var payload = mapper.readTree(message.getPayload());
        var broadcast = mapper.createObjectNode();
        broadcast.put("type", "message");
        broadcast.put("from", payload.has("from") ? payload.get("from").asText() : "unknown");
        broadcast.put("text", payload.get("text").asText());
        broadcast.put("timestamp", java.time.LocalDateTime.now().toString());

        TextMessage out = new TextMessage(mapper.writeValueAsString(broadcast));
        for (WebSocketSession s : sessions) {
            if (s.isOpen()) s.sendMessage(out);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }
}
