-- category-service 초기 스키마

CREATE TABLE IF NOT EXISTS categories (
    id          VARCHAR(20)  PRIMARY KEY,
    name        VARCHAR(50)  NOT NULL UNIQUE,
    description VARCHAR(200),
    icon_url    TEXT,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS evaluation_fields (
    id           BIGSERIAL    PRIMARY KEY,
    category_id  VARCHAR(20)  NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    field_key    VARCHAR(50)  NOT NULL,
    display_name VARCHAR(50)  NOT NULL,
    field_type   VARCHAR(20)  NOT NULL,
    required     BOOLEAN      NOT NULL DEFAULT FALSE,
    sort_order   INT          NOT NULL DEFAULT 0,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT uk_eval_fields_category_key UNIQUE (category_id, field_key)
);

CREATE TABLE IF NOT EXISTS evaluation_field_options (
    field_id     BIGINT       NOT NULL REFERENCES evaluation_fields(id) ON DELETE CASCADE,
    option_value VARCHAR(100) NOT NULL,
    PRIMARY KEY (field_id, option_value)
);

CREATE INDEX idx_categories_active ON categories(is_active);
CREATE INDEX idx_eval_fields_category ON evaluation_fields(category_id);
