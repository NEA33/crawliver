CREATE TABLE page(
  id BIGSERIAL,
  url VARCHAR (6144) NOT NULL,
  domain VARCHAR (128) NOT NULL,
  sub_domain VARCHAR (128) NOT NULL,
  type VARCHAR (128),
  encoding VARCHAR (32),
  language VARCHAR (8),
  status_code INTEGER,
  bytes_length INTEGER,
  total_links INTEGER,
  external_links INTEGER,
  text_size INTEGER,
  html_size INTEGER,
  title VARCHAR (256),
  stamp TIMESTAMP NOT NULL,
  PRIMARY KEY (id)
);