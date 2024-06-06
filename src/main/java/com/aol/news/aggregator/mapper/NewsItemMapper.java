package com.aol.news.aggregator.mapper;

import com.aol.news.aggregator.entity.NewsItem;
import com.aol.news.aggregator.v1.generated.api.model.NewsItemResponse;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsItemMapper {

  @Mapping(source = "publisher.name", target = "publisher")
  NewsItemResponse toNewsItemResponse(NewsItem newsItem);

  List<NewsItemResponse> toNewsItemsResponse(List<NewsItem> newsItems);
}
