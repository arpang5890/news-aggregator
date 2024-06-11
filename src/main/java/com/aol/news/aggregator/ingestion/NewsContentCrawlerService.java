package com.aol.news.aggregator.ingestion;

import com.aol.news.aggregator.ingestion.crawler.CrawlerConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class NewsContentCrawlerService {

  private final CrawlerConfig crawlerConfig;
  private static final int MAX_ATTEMPTS = 3;

  public String fetchArticleContent(String newsPublisherId, String newsItemLink) {
    return Optional.ofNullable(
            crawlerConfig.getCrawlerHandlers().get(newsPublisherId.toUpperCase()))
        .map(handler -> handler.extractArticleContent(newsItemLink, MAX_ATTEMPTS))
        .orElse("");
  }
}
