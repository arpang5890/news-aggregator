package com.aol.news.aggregator.resource;

import com.aol.news.aggregator.BaseIT;
import com.aol.news.aggregator.utils.TestDataGenerator;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemPaginatedResponse;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class NewsItemResourceIT extends BaseIT {

  private static final String RESOURCE = "news-items";

  @Test
  void testGetLatestNews_ZeroNewsItems() throws JsonProcessingException {
    List<NewsPublisherResponse> newsPublishers =
        objectMapper.readValue(
            this.restTemplate.getForObject(getApiUrl(RESOURCE + "/latest"), String.class),
            new TypeReference<>() {});
    assertThat(newsPublishers).isNotNull();
    assertThat(newsPublishers).isEmpty();
  }

  @Test
  void testGetNewsBySource_ZeroNewsItems() {
    createTestNewsItemPerSource();
    NewsItemPaginatedResponse newsItemResponses =
        this.restTemplate.getForObject(
            getApiUrl(RESOURCE + "?source=TEST"), NewsItemPaginatedResponse.class);
    assertThat(newsItemResponses).isNotNull();
    assertThat(newsItemResponses.getCount()).isEqualTo(0);
  }

  @Test
  void testGetNewsBySource_NonZeroNewsItems() {
    createTestNewsItemPerSource();
    NewsItemPaginatedResponse newsItemResponses =
        this.restTemplate.getForObject(
            getApiUrl(RESOURCE + "?source=NYT"), NewsItemPaginatedResponse.class);
    assertThat(newsItemResponses).isNotNull();
    assertThat(newsItemResponses.getCount()).isEqualTo(2);
  }

  @Test
  void testGetNewsBySourcePagination_NonZeroNewsItemsWithPagination() {
    createTestNewsItemPerSource();
    NewsItemPaginatedResponse newsItemResponses =
        this.restTemplate.getForObject(
            getApiUrl(RESOURCE + "?source=FT&offset=0&limit=1"), NewsItemPaginatedResponse.class);
    assertThat(newsItemResponses).isNotNull();
    assertThat(newsItemResponses.getCount()).isEqualTo(2);
    assertThat(newsItemResponses.getItems()).hasSize(1);
  }

  private void createTestNewsItemPerSource() {
    newsItemRepository.saveAll(
        TestDataGenerator.createTestNewsItems(
            newsPublisherRepository.saveAll(
                TestDataGenerator.createTestNewsPublishers(Set.of("NYT", "FT", "BBC"))),
            2));
  }
}
