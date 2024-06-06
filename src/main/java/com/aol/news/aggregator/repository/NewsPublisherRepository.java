package com.aol.news.aggregator.repository;

import com.aol.news.aggregator.entity.NewsPublisher;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsPublisherRepository extends JpaRepository<NewsPublisher, String> {

  List<NewsPublisher> findByEnabledTrue();
}
