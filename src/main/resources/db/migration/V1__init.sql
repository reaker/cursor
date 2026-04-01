CREATE TABLE IF NOT EXISTS fuel_product (
    id            INTEGER PRIMARY KEY,
    name_pl       TEXT        NOT NULL,
    name_en       TEXT        NOT NULL,
    symbol        TEXT        NOT NULL
);

CREATE TABLE IF NOT EXISTS fuel_price (
    id              BIGSERIAL PRIMARY KEY,
    product_id      INTEGER     NOT NULL REFERENCES fuel_product(id),
    product_symbol  TEXT        NOT NULL,
    effective_date  DATE        NOT NULL,
    publish_from    TIMESTAMPTZ NOT NULL,
    price_value     NUMERIC(12,4) NOT NULL,
    location_name   TEXT        NULL,
    location_symbol TEXT        NULL,
    unit            TEXT        NULL,

    CONSTRAINT uq_fuel_price_product_date UNIQUE (product_id, effective_date)
);

CREATE INDEX IF NOT EXISTS idx_fuel_price_product_date
    ON fuel_price(product_id, effective_date);

