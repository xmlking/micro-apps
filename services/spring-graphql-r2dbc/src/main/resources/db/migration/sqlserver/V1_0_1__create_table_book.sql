-- CREATE DATABASE mytestdb
-- GO
-- USE mytestdb
-- GO


CREATE TABLE books (
    Id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT NEWID(),
    title VARCHAR(255) NOT NULL,
    pages DECIMAL(10),
--     category ENUM('HORROR', 'COMEDY', 'FANTASY') NOT NULL,
    category varchar(255) check (category in ('HORROR', 'COMEDY', 'FANTASY')),
    created_at datetimeoffset NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at datetimeoffset,
    updated_by VARCHAR(255),
    version int not null
);

CREATE TABLE authors (
    Id UNIQUEIDENTIFIER NOT NULL PRIMARY KEY DEFAULT NEWID(),
    name VARCHAR(255) NOT NULL,
    age DECIMAL(10),
    version int not null,
    book_id UNIQUEIDENTIFIER not null,
    foreign key (book_id) references books(id)
);
