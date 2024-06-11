package com.aol.news.aggregator.ingestion.crawler;

import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

@Slf4j
public class NYTNewsCrawlerHandler extends NewsArticleCrawlerHandler {

  private static final By ARTICLE_BODY_SELECTOR = By.cssSelector("section[name='articleBody']");
  private static final By PARAGRAPH_TAG = By.tagName("p");

  public NYTNewsCrawlerHandler(final WebDriver webDriver) {
    super(webDriver);
  }

  @Override
  protected String getContent(WebDriverWait wait) {
    WebElement articleElement =
        wait.until(ExpectedConditions.presenceOfElementLocated(ARTICLE_BODY_SELECTOR));
    List<WebElement> paragraphs = articleElement.findElements(PARAGRAPH_TAG);
    StringBuilder contentBuilder = new StringBuilder();
    paragraphs.forEach(paragraph -> contentBuilder.append(paragraph.getText()).append("\n"));
    return contentBuilder.toString();
  }
}
