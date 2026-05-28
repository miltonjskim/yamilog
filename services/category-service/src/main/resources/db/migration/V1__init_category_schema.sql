-- category-service 초기 스키마

CREATE TABLE IF NOT EXISTS categories (
    seq_id      BIGSERIAL    NOT NULL,
    id          UUID         NOT NULL,
    name        VARCHAR(50)  NOT NULL,
    description VARCHAR(200),
    icon_url    TEXT,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    CONSTRAINT pk_categories PRIMARY KEY (seq_id),
    CONSTRAINT uk_categories_public_id UNIQUE (id),
    CONSTRAINT uk_categories_name UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS evaluation_fields (
    id              BIGSERIAL    NOT NULL,
    category_seq_id BIGINT       NOT NULL,
    field_key       VARCHAR(50)  NOT NULL,
    display_name    VARCHAR(50)  NOT NULL,
    field_type      VARCHAR(20)  NOT NULL,
    required        BOOLEAN      NOT NULL DEFAULT FALSE,
    sort_order      INT          NOT NULL DEFAULT 0,
    CONSTRAINT pk_evaluation_fields PRIMARY KEY (id),
    CONSTRAINT fk_eval_fields_category FOREIGN KEY (category_seq_id)
        REFERENCES categories(seq_id) ON DELETE CASCADE,
    CONSTRAINT uk_eval_fields_category_key UNIQUE (category_seq_id, field_key)
);

CREATE TABLE IF NOT EXISTS evaluation_field_options (
    field_id     BIGINT       NOT NULL,
    option_value VARCHAR(100) NOT NULL,
    CONSTRAINT pk_eval_field_options PRIMARY KEY (field_id, option_value),
    CONSTRAINT fk_eval_field_options_field FOREIGN KEY (field_id)
        REFERENCES evaluation_fields(id) ON DELETE CASCADE
);

CREATE INDEX idx_categories_active ON categories(is_active);
CREATE INDEX idx_eval_fields_category ON evaluation_fields(category_seq_id);
