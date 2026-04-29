package com.project.radix.Config;

import com.project.radix.Service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@RequiredArgsConstructor
public class DoctorAlertWebSocketHandler extends TextWebSocketHandler {

    private final WebSocketNotificationService notificationService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        notificationService.afterConnectionEstablished(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        // Clients don't send messages, just receive
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        notificationService.afterConnectionClosed(session, status);
    }
}