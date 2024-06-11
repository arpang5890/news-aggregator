package com.aol.news.aggregator.ingestion.crawler;

import java.time.Duration;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
public class BBCNewsArticleCrawlerHandler extends NewsArticleCrawlerHandler {

  private static final By ARTICLE_SELECTOR = By.cssSelector("article");
  private static final By PARAGRAPH_TAG = By.tagName("p");
  private static final Duration TIMEOUT = Duration.ofSeconds(20);

  public BBCNewsArticleCrawlerHandler(final WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  protected String getContent(String url) {
    webDriver.get(url);
    WebDriverWait wait = new WebDriverWait(webDriver, TIMEOUT);
    WebElement articleElement =
        wait.until(ExpectedConditions.presenceOfElementLocated(ARTICLE_SELECTOR));
    List<WebElement> paragraphs = articleElement.findElements(PARAGRAPH_TAG);

    StringBuilder contentBuilder = new StringBuilder();
    paragraphs.forEach(paragraph -> contentBuilder.append(paragraph.getText()).append("\n"));

    return contentBuilder.toString();
  }
}
