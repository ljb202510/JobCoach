package com.example.jobcoach.web;

import com.example.jobcoach.application.MatchAnalysisService;
import com.example.jobcoach.domain.MatchReport;
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
    private final MatchAnalysisService matchAnalysisService;

    public MatchController(MatchAnalysisService matchAnalysisService) {
        this.matchAnalysisService = matchAnalysisService;
    }

    @PostMapping
    public ResponseEntity<MatchReport> analyze(@Valid @RequestBody MatchRequest request) {
        return ResponseEntity.ok(matchAnalysisService.analyze(request.jobDescription(), request.profile()));
    }

    public record MatchRequest(
            @NotBlank @Size(max = 20_000) String jobDescription,
            @NotBlank @Size(max = 20_000) String profile) {
    }
}
