package com.aol.news.aggregator;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
public class NewsAggregatorApplication {

  public static void main(String[] args) {
    SpringApplication.run(NewsAggregatorApplication.class, args);
  }
}
