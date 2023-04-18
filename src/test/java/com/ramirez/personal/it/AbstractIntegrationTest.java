package com.ramirez.personal.it;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Slf4j
public abstract class AbstractIntegrationTest {
  private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:15.2");
  private static final PostgreSQLContainer postgres;

  static {
    Instant start = Instant.now();

    postgres =
        new PostgreSQLContainer<>(POSTGRES_IMAGE)
            .withDatabaseName("it_postgres")
            .withUsername("fakeUsername")
            .withPassword("fakePassword")
    //        .withCommand("--character-set-server=utf8mb4",
    // "--collation-server=utf8mb4_unicode_ci")
    ;

    Stream.of(postgres).parallel().forEach(GenericContainer::start);

    log.info("🐳 TestContainers started in {}", Duration.between(start, Instant.now()));
  }

  @DynamicPropertySource
  static void properties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
  }
}
