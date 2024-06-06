package com.aol.news.aggregator.ingestion;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.entity.NewsPublisher;
import com.aol.news.aggregator.repository.NewsItemRepository;
import com.aol.news.aggregator.repository.NewsPublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsAggregatorScheduler {

  private final NewsPublisherRepository newsPublisherRepository;
  private final NewsItemRepository newsItemRepository;
  private final RssFeedParser rssFeedParser;

  @Value("${news.aggregator.schedule.enabled}")
  private boolean isEnabled;

  @Scheduled(fixedRateString = "${news.aggregator.schedule.intervalMs}")
  public void syncNewsWithPublishers() {
    if (!isEnabled) {
      return;
    }

    log.info("News aggregator scheduler started.");
    List<NewsPublisher> publishers = newsPublisherRepository.findByEnabledTrue();
    publishers.forEach(this::syncNews);
    log.info("News aggregator scheduler finished.");
  }

  private void syncNews(NewsPublisher publisher) {
    log.info(
        "Fetching Latest news for publisher id: {}, name: {}",
        publisher.getId(),
        publisher.getName());

    try {
      List<NewsItem> latestNewsItems = rssFeedParser.parse(publisher);
      log.info(
          "News Items parsed: {} from publisher: {}", latestNewsItems.size(), publisher.getName());

      List<NewsItem> dbExistingNews =
          newsItemRepository.findByPublisherIdAndExternalIdIn(
              publisher.getId(),
              latestNewsItems.stream().map(NewsItem::getExternalId).collect(Collectors.toList()));
      List<NewsItem> filteredLatestNewsItems = filterNewFeeds(latestNewsItems, dbExistingNews);

      log.info(
          "Found {} new feeds for publisher: {}",
          filteredLatestNewsItems.size(),
          publisher.getName());
      newsItemRepository.saveAll(filteredLatestNewsItems);
      log.info(
          "Saved {} new feeds for publisher: {}",
          filteredLatestNewsItems.size(),
          publisher.getName());
    } catch (Exception e) {
      log.error(
          "syncNews failed for publisher id: {}, name: {}, url: {}",
          publisher.getId(),
          publisher.getName(),
          publisher.getRssFeedUrl(),
          e);
    }
  }

  private List<NewsItem> filterNewFeeds(
      List<NewsItem> latestNewsItems, List<NewsItem> dbExistingNews) {
    Set<String> existingExternalNewsId =
        dbExistingNews.stream().map(NewsItem::getExternalId).collect(Collectors.toSet());
    return latestNewsItems.stream()
        .filter(feed -> !existingExternalNewsId.contains(feed.getExternalId()))
        .collect(Collectors.toList());
  }
}
