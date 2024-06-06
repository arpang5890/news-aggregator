FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY ../target/news-aggregator.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]