package com.aol.news.aggregator.ingestion.crawler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

@AllArgsConstructor
@Slf4j
public abstract class BaseNewsArticleCrawlerHandler {

  protected final WebDriver webDriver;

  public String extractArticleContent(String url, int maxAttempts) {
    log.info("Getting content for url : {}", url);
    int retries = 0;
    boolean success = false;
    String articleContent = "";

    while (retries < maxAttempts && !success) {
      try {
        articleContent = getContent(url);
        success = true;
      } catch (Exception e) {
        retries++;
        if (retries == maxAttempts) {
          log.error("Failed to extract article content after " + maxAttempts + " attempts", e);
        }
      }
    }
    return articleContent;
  }

  protected abstract String getContent(String url);
}
