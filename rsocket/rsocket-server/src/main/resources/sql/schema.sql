CREATE TABLE IF NOT EXISTS stocks (
    id BIGSERIAL PRIMARY KEY,
    ticker VARCHAR(50) NOT NULL,
    name VARCHAR(255) NOT NULL,
    exchange VARCHAR(255) NOT NULL,
    sector VARCHAR(255) NOT NULL
);