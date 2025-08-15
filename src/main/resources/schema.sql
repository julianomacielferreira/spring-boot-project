CREATE TABLE IF NOT EXISTS Post (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    version INT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Comment (
    id INT NOT NULL,
    post_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    version INT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Album (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    version INT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Photo (
    id INT NOT NULL,
    album_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    url VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255) NOT NULL,
    version INT NULL,
    PRIMARY KEY(id)
);