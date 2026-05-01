package com.project.radix.Controller;

import com.project.radix.Model.User;
import com.project.radix.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/internal-chat")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InternalChatController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public ResponseEntity<List<Map<String, Object>>> getDirectory() {
        var users = userRepository.findAll().stream()
                .map(this::toDirectoryUser)
                .toList();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<Map<String, Object>>> getConversations(@RequestParam(required = false) String type) {
        boolean group = "group".equalsIgnoreCase(type);
        if (group) {
            return ResponseEntity.ok(List.of(
                    Map.of("id", "guardia-nuclear", "title", "Guardia nuclear", "meta", "4 médicos", "excerpt", "Coordinación de alertas y cambios de turno.", "type", "group", "participantsCount", 4),
                    Map.of("id", "terapia-i131", "title", "Comité terapia I-131", "meta", "3 médicos", "excerpt", "Seguimiento compartido de tratamientos activos.", "type", "group", "participantsCount", 3)
            ));
        }
        return ResponseEntity.ok(userRepository.findAll().stream()
                .filter(user -> !"patient".equalsIgnoreCase(user.getRole()))
                .limit(6)
                .map(user -> Map.<String, Object>of(
                        "id", user.getId(),
                        "title", user.getFirstName() + " " + user.getLastName(),
                        "meta", user.getSpecialty() != null ? user.getSpecialty() : user.getRole(),
                        "excerpt", "Conversación clínica directa.",
                        "type", "direct",
                        "participantsCount", 2
                ))
                .toList());
    }

    @GetMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<List<Map<String, Object>>> getMessages(@PathVariable String conversationId) {
        return ResponseEntity.ok(List.of(
                Map.of(
                        "id", "msg-" + conversationId + "-1",
                        "senderName", "Radix Admin",
                        "senderAvatarId", 12,
                        "messageText", "He actualizado el contexto clínico de la conversación.",
                        "sentAt", LocalDateTime.now().minusMinutes(12).toString(),
                        "online", true
                ),
                Map.of(
                        "id", "msg-" + conversationId + "-2",
                        "senderName", "Elena Ruiz",
                        "senderAvatarId", 32,
                        "messageText", "Perfecto, reviso las métricas antes del cierre de turno.",
                        "sentAt", LocalDateTime.now().minusMinutes(4).toString(),
                        "online", true
                )
        ));
    }

    @PostMapping("/conversations/{conversationId}/messages")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @PathVariable String conversationId,
            @RequestBody Map<String, String> body
    ) {
        String text = body.getOrDefault("messageText", "").trim();
        if (text.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "messageText is required"));
        }
        return ResponseEntity.ok(Map.of(
                "id", "msg-" + System.currentTimeMillis(),
                "senderName", "Tú",
                "senderAvatarId", 7,
                "messageText", text,
                "sentAt", LocalDateTime.now().toString(),
                "online", true
        ));
    }

    private Map<String, Object> toDirectoryUser(User user) {
        return Map.of(
                "id", user.getId(),
                "name", (user.getFirstName() + " " + user.getLastName()).trim(),
                "role", user.getSpecialty() != null ? user.getSpecialty() : user.getRole(),
                "status", "patient".equalsIgnoreCase(user.getRole()) ? "Paciente" : user.getRole()
        );
    }
}
