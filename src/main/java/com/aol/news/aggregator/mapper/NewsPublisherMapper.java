package com.aol.news.aggregator.mapper;

import com.aol.news.aggregator.entity.NewsPublisher;
import com.aol.news.aggregator.v1.generated.api.model.NewsPublisherResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsPublisherMapper {

  List<NewsPublisherResponse> toNewsItemResponse(List<NewsPublisher> newsPublishers);
}
