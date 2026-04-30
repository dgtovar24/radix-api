package com.project.radix.Config;

import com.project.radix.Service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final DoctorAlertWebSocketHandler alertHandler;
    private final ChatWebSocketHandler chatHandler;
    private final RixWebSocketHandler rixHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(alertHandler, "/ws/alerts").setAllowedOrigins("*");
        registry.addHandler(chatHandler, "/ws/chat").setAllowedOrigins("*");
        registry.addHandler(rixHandler, "/ws/rix").setAllowedOrigins("*");
    }
}
