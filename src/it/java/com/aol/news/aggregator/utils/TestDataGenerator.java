package com.aol.news.aggregator.utils;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.entity.NewsPublisher;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestDataGenerator {

  public static List<NewsPublisher> createTestNewsPublishers(Set<String> newsPublishersId) {
    return newsPublishersId.stream()
        .map(TestDataGenerator::buildNewsPublisher)
        .collect(Collectors.toList());
  }

  public static List<NewsItem> createTestNewsItems(
      List<NewsPublisher> newsPublishers, int newsCountPerSource) {
    return newsPublishers.stream()
        .flatMap(
            newsPublisher ->
                IntStream.range(0, newsCountPerSource).mapToObj(i -> buildNewsItem(newsPublisher)))
        .collect(Collectors.toList());
  }

  private static NewsPublisher buildNewsPublisher(String id) {
    NewsPublisher publisher = new NewsPublisher();
    publisher.setId(id);
    publisher.setName("Test name: " + id);
    publisher.setEnabled(true);
    publisher.setRssFeedUrl("test url");
    return publisher;
  }

  private static NewsItem buildNewsItem(NewsPublisher newsPublisher) {
    NewsItem newsItem = new NewsItem();
    newsItem.setPubDate(OffsetDateTime.now());
    newsItem.setLink("test link " + newsPublisher.getId());
    newsItem.setTitle("test link " + newsPublisher.getId());
    newsItem.setDescription("test link " + newsPublisher.getId());
    newsItem.setExternalId("externalId" + UUID.randomUUID());
    newsItem.setContent("test content " + newsPublisher.getId());
    newsItem.setPublisher(newsPublisher);
    return newsItem;
  }
}
