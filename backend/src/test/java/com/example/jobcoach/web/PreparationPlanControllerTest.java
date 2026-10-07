package com.example.jobcoach.web;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jobcoach.ai.provider=fake")
class PreparationPlanControllerTest {
    @LocalServerPort
    int port;

    private final HttpClient client = HttpClient.newHttpClient();

    @Test
    void returnsReadOnlyPreview() throws Exception {
        var response = post("""
                {"matchScore":72,"requirements":[],"evidence":[],
                 "skillGaps":[{"skill":"Spring Boot","reason":"Need a tested API","priority":"HIGH"}],
                 "risks":[],"recommendations":["Build a project"]}
                """);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("\"status\":\"PREVIEW\""));
        assertTrue(response.body().contains("\"id\":null"));
        assertTrue(response.body().contains("\"matchId\":null"));
        assertTrue(response.body().contains("Spring Boot"));
    }

    @Test
    void rejectsMissingTaskInputs() throws Exception {
        var response = post("{\"matchScore\":72,\"skillGaps\":null,\"recommendations\":[]}");
        assertEquals(400, response.statusCode());
        assertTrue(response.body().contains("\"code\":\"INPUT_INVALID\""));
    }

    @Test
    void previewsRecommendationsWhenThereAreNoSkillGaps() throws Exception {
        var response = post("""
                {"matchScore":90,"requirements":[],"evidence":[],"skillGaps":[],
                 "risks":[],"recommendations":["Practice an API interview"]}
                """);
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Practice an API interview"));
    }

    private HttpResponse<String> post(String body) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://localhost:" + port + "/api/preparation-plans/preview"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
