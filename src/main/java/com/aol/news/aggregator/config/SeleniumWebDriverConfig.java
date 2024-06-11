package com.aol.news.aggregator.config;

import com.aol.news.aggregator.ingestion.crawler.BBCNewsArticleCrawlerHandler;
import com.aol.news.aggregator.ingestion.crawler.CrawlerConfig;
import com.aol.news.aggregator.ingestion.crawler.NYTNewsCrawlerHandler;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SeleniumWebDriverConfig {

  @Bean
  @ConditionalOnProperty(name = "selenium.enabled", havingValue = "true")
  public CrawlerConfig getCrawlerConfig() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless"); // Enable headless mode
    options.addArguments("--disable-gpu"); // Disable GPU acceleration
    options.addArguments("--window-size=1920,1080"); // Set window size for consistency
    WebDriver webDriver = new ChromeDriver(options);
    CrawlerConfig crawlerConfig = new CrawlerConfig();
    crawlerConfig.addCrawlerHandler("NYT", new NYTNewsCrawlerHandler(webDriver));
    crawlerConfig.addCrawlerHandler("NYT", new BBCNewsArticleCrawlerHandler(webDriver));
    WebDriverManager.chromedriver().setup();
    return crawlerConfig;
  }

  @Bean
  @ConditionalOnProperty(name = "selenium.enabled", havingValue = "false")
  public CrawlerConfig getEmptyCrawlerConfig() {
    return new CrawlerConfig();
  }
}
