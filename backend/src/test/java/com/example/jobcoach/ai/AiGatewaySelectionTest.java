package com.example.jobcoach.ai;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest(properties = {
        "jobcoach.ai.provider=real",
        "jobcoach.ai.base-url=http://localhost:9999/v1",
        "jobcoach.ai.model=test-model"
})
class AiGatewaySelectionTest {
    @Autowired
    AiGateway gateway;

    @Test
    void selectsRealGatewayFromProviderProperty() {
        assertInstanceOf(RealAiGateway.class, gateway);
    }
}
