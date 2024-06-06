package com.aol.news.aggregator.resource;

import com.aol.news.aggregator.BaseIT;
import com.aol.news.aggregator.utils.TestDataGenerator;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Set;

public class NewsPublisherResourceIT extends BaseIT {

  @Autowired private TestRestTemplate restTemplate;
  private static final String RESOURCE = "news-publishers";

  @Test
  void testGetAllNewsPublishers_NonZeroNewsPublishers() throws JsonProcessingException {
    newsPublisherRepository.deleteAll();
    List<NewsPublisherResponse> newsPublishers =
        objectMapper.readValue(
            this.restTemplate.getForObject(getApiUrl(RESOURCE), String.class),
            new TypeReference<>() {});
    assertThat(newsPublishers).isNotNull();
    assertThat(newsPublishers).isEmpty();
  }

  @Test
  void testGetAllNewsPublishers_nonEmpty() throws JsonProcessingException {
    newsPublisherRepository.saveAll(
        TestDataGenerator.createTestNewsPublishers(Set.of("NYT", "FT", "BBC")));
    String response = this.restTemplate.getForObject(getApiUrl(RESOURCE), String.class);
    TypeReference<List<NewsPublisherResponse>> typeRef = new TypeReference<>() {};
    List<NewsPublisherResponse> newsPublishers = objectMapper.readValue(response, typeRef);
    assertThat(newsPublishers).isNotNull();
    assertThat(newsPublishers).hasSize(3);
  }
}
