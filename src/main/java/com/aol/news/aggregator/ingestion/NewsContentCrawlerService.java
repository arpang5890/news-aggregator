package com.aol.news.aggregator.ingestion;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NewsContentCrawlerService {

  /**
   * Fetches the content from the given URL.
   *
   * @param url the URL of the news content to fetch
   * @return the fetched content as a String
   *     <p>Note: This method is currently not working as expected. Looking for alternative
   *     solutions due to rate limits + cloudflare security
   */
  public String fetchContent(String url) {
    try {
      Document document = Jsoup.connect(url).get();
      Element thumbsBlock = document.selectFirst("div.THUMBS-BLOCK");

      if (thumbsBlock != null) {
        Elements links = thumbsBlock.select("a[href]");
        if (!links.isEmpty()) {
          Element lastLink = links.last();
          String lastLinkHref = lastLink.attr("href");
          // Fetch content from the last href link
          Document linkDocument = Jsoup.connect(lastLinkHref).get();
          Element article = linkDocument.selectFirst("article");
          if (article != null) {
            String content = article.text();
            log.info("article found for url : {}", url);
            return content;
          }
        }
      } else {
        log.info("No content found for url : {}", url);
        return null;
      }
      return null;
    } catch (Exception e) {
      log.error("Error while fetching content from news URL: {}", url, e);
      return null;
    }
  }
}
