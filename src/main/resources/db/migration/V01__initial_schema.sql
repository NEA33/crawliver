CREATE TABLE page
(
    id               SERIAL,
    domain           VARCHAR(256),
    url              VARCHAR(5120) NOT NULL,
    language         VARCHAR(4),
    bytes_length     INTEGER,
    total_links      INTEGER,
    external_links   INTEGER,
    sub_domain_links INTEGER,
    text_size        INTEGER,
    html_size        INTEGER,
    title            VARCHAR(128),
    stamp            TIMESTAMP     NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE domain
(
    id     SERIAL,
    name   VARCHAR(128) NOT NULL,
    number INTEGER,
    PRIMARY KEY (id)
);

INSERT into domain(name, number)
VALUES ('external', 0);