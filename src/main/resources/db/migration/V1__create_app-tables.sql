CREATE TABLE news_publisher (
    id VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    rss_feed_url VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE news_item (
    guid VARCHAR(255) NOT NULL,
    publisher_id VARCHAR(50) NOT NULL,
    external_id VARCHAR(500) NOT NULL,
    title TEXT NOT NULL,
    link TEXT NOT NULL,
    description MEDIUMTEXT,
    pub_date TIMESTAMP NOT NULL,
    content TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (guid)
);

CREATE INDEX idx_publisher_id ON news_item (publisher_id);
CREATE INDEX idx_external_id ON news_item (external_id);

