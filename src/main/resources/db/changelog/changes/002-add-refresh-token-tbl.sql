CREATE TABLE refresh_tokens(
    id SERIAL PRIMARY KEY,
    token uuid,
    expires_at TIMESTAMP,
    user_id INT REFERENCES users(id)
)