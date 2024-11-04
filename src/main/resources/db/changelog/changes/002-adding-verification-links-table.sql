CREATE TABLE if not exists  verification_links (
    id serial not null primary key,
    verification_token UUID,
    expired_at timestamp default current_timestamp,
    player_id int references players(id) not null
);