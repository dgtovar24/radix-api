package com.project.radix.Controller;

import com.project.radix.DTO.MessageRequest;
import com.project.radix.DTO.MessageResponse;
import com.project.radix.Model.MotivationalMessage;
import com.project.radix.Repository.MotivationalMessageRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MessageController {

    private final MotivationalMessageRepository messageRepository;

    private MessageResponse toResponse(MotivationalMessage m) {
        return MessageResponse.builder()
                .id(m.getId())
                .patientId(m.getFkPatientId())
                .messageText(m.getMessageText())
                .isRead(m.getIsRead())
                .sentAt(m.getSentAt())
                .build();
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<MessageResponse>> getByPatient(@PathVariable Integer patientId) {
        return ResponseEntity.ok(messageRepository.findByFkPatientIdOrderBySentAtDesc(patientId)
                .stream().map(this::toResponse).toList());
    }

    @PostMapping
    public ResponseEntity<MessageResponse> send(@Valid @RequestBody MessageRequest request) {
        MotivationalMessage m = new MotivationalMessage();
        m.setFkPatientId(request.getFkPatientId());
        m.setMessageText(request.getMessageText());
        m = messageRepository.save(m);
        return ResponseEntity.status(201).body(toResponse(m));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Integer id) {
        return messageRepository.findById(id)
                .map(m -> {
                    m.setIsRead(true);
                    messageRepository.save(m);
                    return ResponseEntity.ok((Object) toResponse(m));
                })
                .orElse(ResponseEntity.status(404).body(Map.of("error", "Message not found")));
    }
}
