package com.aol.news.aggregator.service;

import com.aol.news.aggregator.entity.NewsPublisher;
import com.aol.news.aggregator.mapper.NewsPublisherMapper;
import com.aol.news.aggregator.repository.NewsPublisherRepository;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsPublisherService {

  private final NewsPublisherRepository newsPublisherRepository;
  private final NewsPublisherMapper newsPublisherMapper;

  public List<NewsPublisherResponse> findActiveNewsPublishers() {
    List<NewsPublisher> newsPublishers = newsPublisherRepository.findByEnabledTrue();
    return newsPublisherMapper.toNewsItemResponse(newsPublishers);
  }
}
