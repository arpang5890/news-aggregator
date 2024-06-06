package com.aol.news.aggregator.resource;

import com.aol.news.aggregator.service.NewsPublisherService;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import com.aol.news.aggregator.v1.resource.NewsPublisherApi;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsPublisherResource implements NewsPublisherApi {

  private final NewsPublisherService newsPublisherService;

  @Override
  public ResponseEntity<List<NewsPublisherResponse>> getAllNewsPublishers() {
    return ResponseEntity.ok(newsPublisherService.findActiveNewsPublishers());
  }
}
