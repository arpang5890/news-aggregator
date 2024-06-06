package com.aol.news.aggregator;

import com.aol.news.aggregator.repository.NewsItemRepository;
import com.aol.news.aggregator.repository.NewsPublisherRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseIT {

  @LocalServerPort private int port;
  @Autowired protected NewsPublisherRepository newsPublisherRepository;
  @Autowired protected NewsItemRepository newsItemRepository;
  @Autowired protected TestRestTemplate restTemplate;
  @Autowired protected ObjectMapper objectMapper;

  @Container
  private static final MySQLContainer<?> mySQLContainer =
      new MySQLContainer<>("mysql:8.0.26")
          .withDatabaseName("news_test")
          .withUsername("user")
          .withPassword("passwd");

  @DynamicPropertySource
  static void registerDynamicProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
    registry.add("spring.datasource.username", mySQLContainer::getUsername);
    registry.add("spring.datasource.password", mySQLContainer::getPassword);
    registry.add("spring.datasource.hikari.initializationFailTimeout", () -> "60000");
    registry.add("spring.datasource.hikari.connection-test-query", () -> "SELECT 1");
    registry.add("news.aggregator.schedule.enabled", () -> "false");
  }

  @AfterEach
  public void clear() {
    newsItemRepository.deleteAll();
    newsPublisherRepository.deleteAll();
  }

  protected String getApiUrl(String resource) {
    return "http://localhost:" + port + "/api/" + resource;
  }
}
