DROP TABLE IF EXISTS article_media;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS outbox;

CREATE TABLE category (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(50) NOT NULL,
    slug VARCHAR(50) NOT NULL,
    description VARCHAR(200) NOT NULL,
    image VARCHAR(512) NOT NULL,
    url VARCHAR(512) NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT category_name_uk UNIQUE (name),
    CONSTRAINT category_slug_uk UNIQUE (slug)
)ENGINE=InnoDB;

CREATE TABLE media (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    description VARCHAR(300) NOT NULL,
    image VARCHAR(512) NOT NULL,
    url VARCHAR(512) NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT media_name_uk UNIQUE (name),
    CONSTRAINT media_slug_uk UNIQUE (slug)
)ENGINE=InnoDB;

CREATE TABLE article (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL,
    description VARCHAR(500) NOT NULL,
    image VARCHAR(512) NOT NULL,
    url VARCHAR(512) NOT NULL,
    properties JSON NOT NULL,
    category_id BIGINT NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT article_name_uk UNIQUE (name),
    CONSTRAINT article_slug_uk UNIQUE (slug),
    CONSTRAINT fk_article_category FOREIGN KEY (category_id) REFERENCES category(id)
)ENGINE=InnoDB;

CREATE TABLE article_media (
    article_id BIGINT NOT NULL,
    media_id BIGINT NOT NULL,
    PRIMARY KEY (article_id, media_id),
    CONSTRAINT fk_article_media_article FOREIGN KEY (article_id) REFERENCES article(id) ON DELETE CASCADE,
    CONSTRAINT fk_article_media_media FOREIGN KEY (media_id) REFERENCES media(id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE outbox (
    id BIGINT AUTO_INCREMENT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL,
    entity_id BIGINT NOT NULL,
    entity_type VARCHAR(32) NOT NULL,
    event_type VARCHAR(32) NOT NULL,
    message JSON NOT NULL,
    partition_key VARCHAR(8) NOT NULL,
    status VARCHAR(32) NOT NULL,
    updated_at DATETIME,
    version INTEGER DEFAULT 0 NOT NULL,
    PRIMARY KEY (id, partition_key),
    CONSTRAINT outbox_uk UNIQUE (id, partition_key)
) ENGINE=InnoDB;
