CREATE TABLE IF NOT EXISTS Post (
    id INT NOT NULL,
    userId INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT NOT NULL,
    version INT NOT NULL,
    PRIMARY KEY (id)
);