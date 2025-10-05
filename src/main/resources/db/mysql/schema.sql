DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS user_role;
DROP TABLE IF EXISTS role_action;
DROP TABLE IF EXISTS article_media;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS media;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS app_user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS action;
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

CREATE TABLE action (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(64) NOT NULL,
    target VARCHAR(256) NOT NULL,
    description VARCHAR(128) NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT action_name_uk UNIQUE (name)
)ENGINE=InnoDB;

CREATE TABLE role (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    name VARCHAR(32) NOT NULL,
    description VARCHAR(128) NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT role_name_uk UNIQUE (name)
)ENGINE=InnoDB;

CREATE TABLE app_user (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    username VARCHAR(32) NOT NULL,
    email VARCHAR(64) NOT NULL,
    password VARCHAR(128) NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT user_username_uk UNIQUE (username),
    CONSTRAINT user_email_uk UNIQUE (email)
)ENGINE=InnoDB;

CREATE TABLE session (
    id BIGINT NOT NULL AUTO_INCREMENT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    token VARCHAR(64) NOT NULL,
    expires_at DATETIME NOT NULL,
    status ENUM('ACTIVE', 'CLOSED') NOT NULL DEFAULT 'ACTIVE',
    user_id BIGINT NOT NULL,
    updated_at DATETIME DEFAULT NULL,
    version INT NOT NULL DEFAULT 0,
    PRIMARY KEY (id),
    CONSTRAINT session_token_uk UNIQUE (token),
    CONSTRAINT fk_session_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE role_action (
    role_id BIGINT NOT NULL,
    action_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, action_id),
    CONSTRAINT fk_role_action_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE,
    CONSTRAINT fk_role_action_action FOREIGN KEY (action_id) REFERENCES action(id) ON DELETE CASCADE
)ENGINE=InnoDB;

CREATE TABLE user_role (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES app_user(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE
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
