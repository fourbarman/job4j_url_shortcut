CREATE TABLE IF NOT EXISTS shortcuts
(
    id        SERIAL PRIMARY KEY,
    url       VARCHAR(2000) UNIQUE NOT NULL,
    code      VARCHAR(2000) UNIQUE NOT NULL,
    total     INTEGER,
    client_id INTEGER REFERENCES clients (id)
);