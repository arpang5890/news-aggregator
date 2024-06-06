package com.aol.news.aggregator.service;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.mapper.NewsItemMapper;
import com.aol.news.aggregator.repository.NewsItemRepository;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemPaginatedResponse;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemResponse;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class NewsItemService {

  private final NewsPublisherService newsPublisherService;
  private final NewsItemRepository newsItemRepository;
  private final NewsItemMapper newsItemMapper;
  private static final int LATEST_NEWS_LIMIT_PER_NEWS_PUBLISHER = 5;

  public NewsItemPaginatedResponse findNewsByPublisher(
      final String source,
      final OffsetDateTime from,
      final OffsetDateTime to,
      final Integer offset,
      final Integer limit) {
    final Specification<NewsItem> spec = buildNewsItemSpecification(source, from, to);
    Page<NewsItem> newsItems =
        newsItemRepository.findAll(
            spec, PageRequest.of(offset, limit, Sort.by("pubDate").descending()));
    return new NewsItemPaginatedResponse()
        .count(newsItems.getTotalElements())
        .items(newsItemMapper.toNewsItemsResponse(newsItems.getContent()));
  }

  public List<NewsItemResponse> findLatestNews() {
    List<NewsItem> latestNewsItemByAllActivePublishers =
        newsPublisherService.findActiveNewsPublishers().stream()
            .flatMap(
                newPublisher ->
                    newsItemRepository
                        .findAll(
                            buildNewsItemSpecification(newPublisher.getId()),
                            PageRequest.of(
                                0,
                                LATEST_NEWS_LIMIT_PER_NEWS_PUBLISHER,
                                Sort.by("pubDate").descending()))
                        .stream())
            .sorted((item1, item2) -> item2.getPubDate().compareTo(item1.getPubDate()))
            .collect(Collectors.toList());

    return newsItemMapper.toNewsItemsResponse(latestNewsItemByAllActivePublishers);
  }

  private Specification<NewsItem> buildNewsItemSpecification(
      String source, OffsetDateTime fromDateTime, OffsetDateTime toDateTime) {
    return (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();
      predicates.add(criteriaBuilder.equal(root.get("publisher").get("id"), source));
      if (fromDateTime != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("pubDate"), fromDateTime));
      }
      if (toDateTime != null) {
        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("pubDate"), toDateTime));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };
  }

  private Specification<NewsItem> buildNewsItemSpecification(String source) {
    return buildNewsItemSpecification(source, null, null);
  }
}
