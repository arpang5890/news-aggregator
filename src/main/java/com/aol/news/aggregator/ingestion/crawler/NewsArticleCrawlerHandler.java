package com.aol.news.aggregator.ingestion.crawler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@AllArgsConstructor
@Slf4j
public abstract class NewsArticleCrawlerHandler {

  protected final WebDriver webDriver;
  private static final Duration TIMEOUT = Duration.ofSeconds(20);

  public String extractArticleContent(String url, int maxAttempts) {
    log.info("Getting content for url : {}", url);
    int retries = 0;
    boolean success = false;
    String articleContent = "";

    while (retries < maxAttempts && !success) {
      try {
        webDriver.get(url);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, TIMEOUT);
        articleContent = getContent(webDriverWait);
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

  protected abstract String getContent(WebDriverWait wait);
}
