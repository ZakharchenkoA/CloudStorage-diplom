package ru.netology.cloudstorage.integration;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql");

    @BeforeAll
    public static void runContainer() {
        MY_SQL_CONTAINER.start();
    }

    @DynamicPropertySource
    static void testProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url=", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username=", MY_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password=", MY_SQL_CONTAINER::getPassword);
        registry.add("spring.liquibase.enabled=", () -> true);
    }
}