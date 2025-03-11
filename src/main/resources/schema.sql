-- Database schema for the Books Service

-- Create extensions
CREATE
EXTENSION IF NOT EXISTS "uuid-ossp";

-- Create tables
CREATE TABLE IF NOT EXISTS categories
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    uuid_generate_v4
(
),
    name VARCHAR
(
    100
) NOT NULL UNIQUE,
    description VARCHAR
(
    500
),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS books
(
    id
    UUID
    PRIMARY
    KEY
    DEFAULT
    uuid_generate_v4
(
),
    title VARCHAR
(
    255
) NOT NULL,
    author VARCHAR
(
    255
) NOT NULL,
    description VARCHAR
(
    2000
),
    isbn VARCHAR
(
    20
) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS books_categories
(
    book_id
    UUID
    NOT
    NULL
    REFERENCES
    books
(
    id
) ON DELETE CASCADE,
    category_id UUID NOT NULL REFERENCES categories
(
    id
)
  ON DELETE CASCADE,
    PRIMARY KEY
(
    book_id,
    category_id
)
    );

-- Create indexes
CREATE INDEX IF NOT EXISTS idx_books_title ON books(title);
CREATE INDEX IF NOT EXISTS idx_books_author ON books(author);
CREATE INDEX IF NOT EXISTS idx_books_isbn ON books(isbn);
CREATE INDEX IF NOT EXISTS idx_categories_name ON categories(name);

-- Create updated_at triggers
CREATE
OR REPLACE FUNCTION update_timestamp()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at
= CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE
OR REPLACE TRIGGER update_books_timestamp
BEFORE
UPDATE ON books
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

CREATE
OR REPLACE TRIGGER update_categories_timestamp
BEFORE
UPDATE ON categories
    FOR EACH ROW
    EXECUTE FUNCTION update_timestamp();

-- Insert sample data
INSERT INTO categories (name, description)
VALUES ('Fiction', 'Fictional literature'),
       ('Science Fiction', 'Science-based speculative fiction'),
       ('Fantasy', 'Fiction with magical or supernatural elements'),
       ('Mystery', 'Fiction dealing with the solution of a crime or puzzle'),
       ('Non-Fiction', 'Informational and factual content') ON CONFLICT (name) DO NOTHING;