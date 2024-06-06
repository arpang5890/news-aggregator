package com.aol.news.aggregator.entity;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

@Data
@NoArgsConstructor
@Entity
@Table(name = "news_item")
public class NewsItem {

  @Id
  @UuidGenerator
  private String guid;

  @ManyToOne
  @JoinColumn(name = "publisher_id")
  private NewsPublisher publisher;

  private String title;
  private String link;
  private String description;
  private String content;
  private OffsetDateTime pubDate;
  private String externalId;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = OffsetDateTime.now();
    updatedAt = OffsetDateTime.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = OffsetDateTime.now();
  }
}
