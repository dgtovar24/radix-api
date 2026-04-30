package com.project.radix.Controller;

import com.project.radix.Model.OAuthClient;
import com.project.radix.Repository.OAuthClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth-clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class OAuthClientController {

    private final OAuthClientRepository oauthClientRepository;

    @GetMapping
    public ResponseEntity<List<OAuthClient>> getAll() {
        return ResponseEntity.ok(oauthClientRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Map<String, String> body) {
        OAuthClient client = new OAuthClient();
        client.setClientId(body.get("clientId"));
        client.setClientSecret(body.get("clientSecret"));
        client.setClientName(body.get("clientName"));
        client.setGrantType(body.get("grantType"));
        client.setScopes(body.get("scopes"));
        client.setIsActive(true);
        client.setCreatedAt(LocalDateTime.now());

        oauthClientRepository.save(client);
        return ResponseEntity.status(201).body(client);
    }
}
