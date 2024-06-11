package com.aol.news.aggregator.ingestion.crawler;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CrawlerConfig {

  private final Map<String, NewsArticleCrawlerHandler> crawlerHandlers;

  public CrawlerConfig() {
    this.crawlerHandlers = new HashMap<>();
  }

  public void addCrawlerHandler(
      String newsPublisherId, NewsArticleCrawlerHandler newsArticleCrawlerHandler) {
    crawlerHandlers.put(newsPublisherId, newsArticleCrawlerHandler);
  }
}
