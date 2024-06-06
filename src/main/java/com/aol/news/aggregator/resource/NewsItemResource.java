package com.aol.news.aggregator.resource;

import com.aol.news.aggregator.service.NewsItemService;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemPaginatedResponse;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemResponse;
import com.aol.news.aggregator.v1.resource.NewsResourceApi;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class NewsItemResource implements NewsResourceApi {

  private final NewsItemService newsItemService;

  @Override
  public ResponseEntity<List<NewsItemResponse>> getLatestNews() {
    return ResponseEntity.ok(newsItemService.findLatestNews());
  }

  @Override
  public ResponseEntity<NewsItemPaginatedResponse> getNewsBySource(
          String source, OffsetDateTime from, OffsetDateTime to, Integer offset, Integer limit) {
    return ResponseEntity.ok(newsItemService.findNewsByPublisher(source, from, to, offset, limit));
  }
}
