package com.aol.news.aggregator.ingestion;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.entity.NewsPublisher;
import java.io.IOException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Slf4j
@Component
public class RssFeedParser {

  private static final List<DateTimeFormatter> FORMATTERS =
      List.of(
          DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH),
          DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH),
          DateTimeFormatter.RFC_1123_DATE_TIME);

  public List<NewsItem> parse(NewsPublisher publisher)
      throws ParserConfigurationException, IOException, SAXException {
    List<NewsItem> rssFeeds = new ArrayList<>();
    String rssFeedUrl = publisher.getRssFeedUrl();
    log.info("Parsing RSS feed from URL: {}", rssFeedUrl);

    Document doc = fetchRssFeedDocument(rssFeedUrl);
    NodeList items = doc.getElementsByTagName("item");
    log.info("Found {} items in the RSS feed.", items.getLength());

    for (int i = 0; i < items.getLength(); i++) {
      Element item = (Element) items.item(i);
      NewsItem feed = parseNewsItem(item, publisher);
      if (feed != null) {
        rssFeeds.add(feed);
      }
    }

    log.info("Parsed {} RSS feed items for publisher: {}", rssFeeds.size(), publisher.getName());
    return rssFeeds;
  }

  private Document fetchRssFeedDocument(String rssFeedUrl)
      throws ParserConfigurationException, IOException, SAXException {
    URL url = new URL(rssFeedUrl);
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(url.openStream());
    log.info("Fetched RSS feed data from URL: {}", rssFeedUrl);
    return doc;
  }

  private NewsItem parseNewsItem(Element item, NewsPublisher publisher) {
    String externalId = getElementTextContent(item, "guid");
    String title = getElementTextContent(item, "title");
    String link = getElementTextContent(item, "link");
    String description = getElementTextContent(item, "description");
    String pubDateStr = getElementTextContent(item, "pubDate");

    OffsetDateTime pubDate = parsePubDate(pubDateStr);
    if (StringUtils.isNotBlank(title)
        && StringUtils.isNotBlank(link)
        && StringUtils.isNotBlank(externalId)
        && pubDate != null) {
      NewsItem feed = new NewsItem();
      feed.setTitle(title);
      feed.setLink(link);
      feed.setPubDate(pubDate);
      feed.setExternalId(externalId);
      feed.setDescription(description);
      feed.setPublisher(publisher);
      return feed;
    }
    return null;
  }

  private OffsetDateTime parsePubDate(String pubDateStr) {
    if (StringUtils.isBlank(pubDateStr)) {
      return null;
    }
    for (DateTimeFormatter formatter : FORMATTERS) {
      try {
        if (pubDateStr.matches(".*\\+\\d{4}")) {
          pubDateStr = pubDateStr.replaceFirst("(\\+\\d{2})(\\d{2})$", "$1:$2");
        }
        return OffsetDateTime.parse(pubDateStr, formatter);
      } catch (DateTimeParseException e) {
        // continue with next formatter
      }
    }
    log.error("Date string {} is not in a supported format", pubDateStr);
    return null;
  }

  private String getElementTextContent(Element parent, String tagName) {
    NodeList nodeList = parent.getElementsByTagName(tagName);
    if (nodeList.getLength() > 0) {
      return nodeList.item(0).getTextContent();
    }
    log.warn("Element {} not found in the RSS feed item.", tagName);
    return "";
  }
}
