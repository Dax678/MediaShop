package org.example.mediashop.TestConfig;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@Import(ContainersConfig.class)
public class IntegrationTest {

    @Container
    @ServiceConnection
    public static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:15-alpine");
}