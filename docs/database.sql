CREATE DATABASE IF NOT EXISTS librarydb;
USE librarydb;

DROP TABLE IF EXISTS Book;
DROP TABLE IF EXISTS Category;

CREATE TABLE Category (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255)
);

CREATE TABLE Book (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    publish_date DATE NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    pages INT NOT NULL,
    available BIT NOT NULL,
    category_id INT NOT NULL,

    CONSTRAINT fk_book_category
        FOREIGN KEY (category_id)
        REFERENCES Category(category_id)
);

INSERT INTO Category (name, description)
VALUES
('Fantasy', 'Bücher mit Magie und erfundenen Welten'),
('Informatik', 'Bücher über Programmierung und IT');

INSERT INTO Book (title, author, publish_date, price, pages, available, category_id)
VALUES
('Der Hobbit', 'J.R.R. Tolkien', '1937-09-21', 19.90, 310, 1, 1),
('Java Grundlagen', 'Max Mustermann', '2024-01-15', 39.90, 450, 1, 2);

SELECT * FROM Category;
SELECT * FROM Book;