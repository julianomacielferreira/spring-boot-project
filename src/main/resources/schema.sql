--DROP SCHEMA public CASCADE;
--CREATE SCHEMA public;

CREATE TABLE IF NOT EXISTS Geo (
    id INT NOT NULL,
    lat VARCHAR(20) NOT NULL,
    lng VARCHAR(20) NOT NULL,
    version INT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Address (
    id INT NOT NULL,
    geo_id INTEGER NOT NULL,
    street VARCHAR(100) NOT NULL,
    suite VARCHAR(50) NOT NULL,
    city VARCHAR(50) NOT NULL,
    zipcode VARCHAR(20) NOT NULL,
    version INT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS Company (
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    catch_phrase VARCHAR(200) NOT NULL,
    bs VARCHAR(200) NOT NULL,
    version INT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE IF NOT EXISTS public.User (
    id INT NOT NULL,
    address_id INTEGER,
    company_id INTEGER,
    name VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(50) NOT NULL,
    website VARCHAR(100) NOT NULL,
    version INT NULL,
    PRIMARY KEY(id)
);

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

CREATE TABLE IF NOT EXISTS Todo (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title VARCHAR(255) NOT NULL,
    completed BOOLEAN DEFAULT FALSE,
    version INT NULL,
    PRIMARY KEY(id)
);