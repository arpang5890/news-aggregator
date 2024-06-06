package com.aol.news.aggregator.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "news_publisher")
public class NewsPublisher {

  @Id private String id;
  private String name;
  private String rssFeedUrl;
  private boolean enabled;
}
