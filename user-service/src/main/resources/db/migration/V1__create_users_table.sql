CREATE TABLE IF NOT EXISTS users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    birth_date DATE,
    phone VARCHAR(20)
);

CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);

