package com.aol.news.aggregator.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.mapper.NewsItemMapper;
import com.aol.news.aggregator.repository.NewsItemRepository;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemPaginatedResponse;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemResponse;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

public class NewsItemServiceTest {

  @Mock private NewsPublisherService newsPublisherService;

  @Mock private NewsItemRepository newsItemRepository;

  @Mock private NewsItemMapper newsItemMapper;

  @InjectMocks private NewsItemService newsItemService;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  public void testFindNewsByPublisher_ValidParameters() {
    String source = "valid-source";
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = OffsetDateTime.now();
    int offset = 0;
    int limit = 10;
    List<NewsItem> newsItems = Collections.singletonList(new NewsItem());
    Page<NewsItem> page = new PageImpl<>(newsItems);
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(Collections.singletonList(new NewsItemResponse()));

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(1);
    assertThat(response.getItems()).hasSize(1);
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_NullFromAndToDates() {
    String source = "valid-source";
    OffsetDateTime from = null;
    OffsetDateTime to = null;
    int offset = 0;
    int limit = 10;
    List<NewsItem> newsItems = Collections.singletonList(new NewsItem());
    Page<NewsItem> page = new PageImpl<>(newsItems);
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(Collections.singletonList(new NewsItemResponse()));

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(1);
    assertThat(response.getItems()).hasSize(1);
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_FromDateOnly() {
    String source = "valid-source";
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = null;
    int offset = 0;
    int limit = 10;
    List<NewsItem> newsItems = Collections.singletonList(new NewsItem());
    Page<NewsItem> page = new PageImpl<>(newsItems);
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(Collections.singletonList(new NewsItemResponse()));

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(1);
    assertThat(response.getItems()).hasSize(1);
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_ToDateOnly() {
    String source = "valid-source";
    OffsetDateTime from = null;
    OffsetDateTime to = OffsetDateTime.now();
    int offset = 0;
    int limit = 10;
    List<NewsItem> newsItems = Collections.singletonList(new NewsItem());
    Page<NewsItem> page = new PageImpl<>(newsItems);
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(Collections.singletonList(new NewsItemResponse()));

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(1);
    assertThat(response.getItems()).hasSize(1);
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_ZeroNewsItems() {
    String source = "non-existent-source";
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = OffsetDateTime.now();
    int offset = 0;
    int limit = 10;
    Page<NewsItem> page = Page.empty();
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList())).thenReturn(Collections.emptyList());

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(0);
    assertThat(response.getItems()).isEmpty();
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_OffsetAndLimitBoundaries() {
    String source = "valid-source";
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = OffsetDateTime.now();
    int offset = 5;
    int limit = 1;
    List<NewsItem> newsItems = Collections.singletonList(new NewsItem());
    Page<NewsItem> page = new PageImpl<>(newsItems);
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(Collections.singletonList(new NewsItemResponse()));

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(1);
    assertThat(response.getItems()).hasSize(1);
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindNewsByPublisher_InvalidSource() {
    String source = "invalid-source";
    OffsetDateTime from = OffsetDateTime.now().minusDays(1);
    OffsetDateTime to = OffsetDateTime.now();
    int offset = 0;
    int limit = 10;
    Page<NewsItem> page = Page.empty();
    when(newsItemRepository.findAll(any(Specification.class), any(PageRequest.class)))
        .thenReturn(page);
    when(newsItemMapper.toNewsItemsResponse(anyList())).thenReturn(Collections.emptyList());

    NewsItemPaginatedResponse response =
        newsItemService.findNewsByPublisher(source, from, to, offset, limit);

    assertThat(response.getCount()).isEqualTo(0);
    assertThat(response.getItems()).isEmpty();
    verify(newsItemRepository).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindLatestNews_WithActivePublishers() {
    // Setup
    NewsPublisherResponse publisher1 = new NewsPublisherResponse();
    NewsPublisherResponse publisher2 = new NewsPublisherResponse();
    when(newsPublisherService.findActiveNewsPublishers())
        .thenReturn(List.of(publisher1, publisher2));

    NewsItem newsItem1 = new NewsItem();
    newsItem1.setPubDate(OffsetDateTime.now().minusHours(1));
    NewsItem newsItem2 = new NewsItem();
    newsItem2.setPubDate(OffsetDateTime.now().minusHours(2));

    Page<NewsItem> page1 = new PageImpl<>(List.of(newsItem1));
    Page<NewsItem> page2 = new PageImpl<>(List.of(newsItem2));

    doReturn(page1)
        .when(newsItemRepository)
        .findAll(any(Specification.class), any(PageRequest.class));
    doReturn(page2)
        .when(newsItemRepository)
        .findAll(any(Specification.class), any(PageRequest.class));
    when(newsItemMapper.toNewsItemsResponse(anyList()))
        .thenReturn(List.of(new NewsItemResponse(), new NewsItemResponse()));

    // Execution
    List<NewsItemResponse> response = newsItemService.findLatestNews();

    // Verification
    assertThat(response).hasSize(2);
    verify(newsPublisherService).findActiveNewsPublishers();
    verify(newsItemRepository, times(2)).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }

  @Test
  public void testFindLatestNews_SomePublishersWithNoNewsItems() {
    // Setup
    NewsPublisherResponse publisher1 = new NewsPublisherResponse();
    NewsPublisherResponse publisher2 = new NewsPublisherResponse();
    when(newsPublisherService.findActiveNewsPublishers())
        .thenReturn(List.of(publisher1, publisher2));

    NewsItem newsItem1 = new NewsItem();
    newsItem1.setPubDate(OffsetDateTime.now().minusHours(1));

    Page<NewsItem> page1 = new PageImpl<>(List.of(newsItem1));
    Page<NewsItem> page2 = Page.empty();

    doReturn(page1)
        .when(newsItemRepository)
        .findAll(any(Specification.class), any(PageRequest.class));
    doReturn(page2)
        .when(newsItemRepository)
        .findAll(any(Specification.class), any(PageRequest.class));
    when(newsItemMapper.toNewsItemsResponse(anyList())).thenReturn(List.of(new NewsItemResponse()));

    // Execution
    List<NewsItemResponse> response = newsItemService.findLatestNews();

    // Verification
    assertThat(response).hasSize(1);
    verify(newsPublisherService).findActiveNewsPublishers();
    verify(newsItemRepository, times(2)).findAll(any(Specification.class), any(PageRequest.class));
    verify(newsItemMapper).toNewsItemsResponse(anyList());
  }
}
