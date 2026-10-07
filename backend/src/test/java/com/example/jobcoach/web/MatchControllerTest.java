package com.example.jobcoach.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "jobcoach.ai.provider=fake")
class MatchControllerTest {
    @LocalServerPort
    int port;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    @Test
    void returnsStructuredMatchReportOverHttp() throws Exception {
        var response = post("""
                {"jobDescription":"Java 后端工程师","profile":"做过 Java 项目"}
                """);

        org.junit.jupiter.api.Assertions.assertEquals(200, response.statusCode());
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"matchScore\":72"));
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"name\":\"Java\""));
    }

    @Test
    void rejectsBlankJobDescriptionWithUnifiedError() throws Exception {
        var response = post("{\"jobDescription\":\"\",\"profile\":\"Java project\"}");
        org.junit.jupiter.api.Assertions.assertEquals(400, response.statusCode());
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"code\":\"INPUT_INVALID\""));
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"path\":\"/api/matches\""));
    }

    @Test
    void acceptsMaximumInputLength() throws Exception {
        var response = post("{\"jobDescription\":\"" + "a".repeat(20_000)
                + "\",\"profile\":\"profile\"}");
        org.junit.jupiter.api.Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    void rejectsInputLongerThanMaximum() throws Exception {
        var response = post("{\"jobDescription\":\"" + "a".repeat(20_001)
                + "\",\"profile\":\"profile\"}");
        org.junit.jupiter.api.Assertions.assertEquals(400, response.statusCode());
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"code\":\"INPUT_INVALID\""));
    }

    @Test
    void rejectsMalformedJsonWithUnifiedError() throws Exception {
        var response = post("{not-json");
        org.junit.jupiter.api.Assertions.assertEquals(400, response.statusCode());
        org.junit.jupiter.api.Assertions.assertTrue(response.body().contains("\"code\":\"BODY_INVALID\""));
    }

    @Test
    void allowsFrontendDevelopmentOrigin() throws Exception {
        var request = HttpRequest.newBuilder(URI.create(url()))
                .method("OPTIONS", HttpRequest.BodyPublishers.noBody())
                .header("Origin", "http://localhost:5173")
                .header("Access-Control-Request-Method", "POST")
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        org.junit.jupiter.api.Assertions.assertEquals(200, response.statusCode());
        org.junit.jupiter.api.Assertions.assertEquals("http://localhost:5173",
                response.headers().firstValue("Access-Control-Allow-Origin").orElseThrow());
    }

    private HttpResponse<String> post(String body) throws Exception {
        var request = HttpRequest.newBuilder(URI.create(url()))
                .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private String url() {
        return "http://localhost:" + port + "/api/matches";
    }
}
