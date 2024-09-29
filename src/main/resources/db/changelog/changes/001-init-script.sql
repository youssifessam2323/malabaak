-- changeset yessam:231321312321
-- precondition-sql-check expectedResult:0 select count(*) from information_schema where table_name = 'players'
CREATE TABLE players(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(72) NOT NULL,
    account_provider VARCHAR(30) NOT NULL,
    is_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT current_timestamp,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE owners(
    id SERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(72) NOT NULL,
    account_provider VARCHAR(30) NOT NULL,
    is_enabled BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT current_timestamp,
    is_blocked BOOLEAN DEFAULT FALSE,
    is_deleted BOOLEAN DEFAULT FALSE
);

CREATE TABLE locations(
    id serial PRIMARY KEY,
    latitude DOUBLE PRECISION NOT NULL CHECK (latitude BETWEEN -90 AND 90),  -- Latitude must be between -90 and 90
    longitude DOUBLE PRECISION NOT NULL CHECK (longitude BETWEEN -180 AND 180), -- Longitude must be between -180 and 180
    city VARCHAR(100),
    street_name VARCHAR(100),
    street_number VARCHAR(100),
    other_info TEXT
);



CREATE TABLE organizations(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    field_reservation_auto_approval BOOLEAN DEFAULT FALSE,
    location_id INT UNIQUE REFERENCES locations(id),
    owner_id INT REFERENCES owners(id) ON DELETE CASCADE
);

CREATE TABLE fields(
    id SERIAL PRIMARY KEY,
    name VARCHAR(100),
    size VARCHAR(20),
    max_num_of_players INT,
    min_num_of_players INT,
    field_rating INT,
    organization_id INT REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE reservations(
    id serial PRIMARY KEY,
    status VARCHAR(50),
    date DATE,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    amount_to_pay NUMERIC(1000,2),
    player_id INT REFERENCES players(id),
    field_id INT REFERENCES fields(id)
);

CREATE TABLE reports(
    id SERIAL PRIMARY KEY,
    title VARCHAR(100),
    description TEXT,
    reported_by VARCHAR(50),
    reporter_id INT,
    report_status VARCHAR(100),
    reservation_id INT REFERENCES reservations(id)
);


