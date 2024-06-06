package com.aol.news.aggregator.repository;

import com.aol.news.aggregator.entity.NewsItem;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsItemRepository
    extends JpaRepository<NewsItem, UUID>, JpaSpecificationExecutor<NewsItem> {

  List<NewsItem> findByPublisherIdAndExternalIdIn(String publisherId, List<String> externalIds);
}
