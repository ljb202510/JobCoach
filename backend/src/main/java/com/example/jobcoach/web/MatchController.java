package com.example.jobcoach.web;

import com.example.jobcoach.ai.AiGateway;
import com.example.jobcoach.ai.MatchReport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final AiGateway aiGateway;

    public MatchController(AiGateway aiGateway) {
        this.aiGateway = aiGateway;
    }

    @PostMapping
    public ResponseEntity<MatchReport> analyze(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(aiGateway.analyze(request.jobDescription(), request.profile()));
    }

    public record MatchRequest(
            @NotBlank @Size(max = 20_000) String jobDescription,
            @NotBlank @Size(max = 20_000) String profile) {
    }
}
