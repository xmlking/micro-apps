CREATE TABLE IF NOT EXISTS books (
    id UUID NOT NULL DEFAULT random_uuid() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    pages DECIMAL(10),
--     category ENUM('HORROR', 'COMEDY', 'FANTASY') NOT NULL,
    category varchar(255) check (category in ('HORROR', 'COMEDY', 'FANTASY')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_by VARCHAR(255) NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    updated_by VARCHAR(255),
    version int8 not null
);

CREATE TABLE IF NOT EXISTS authors (
    id UUID NOT NULL DEFAULT random_uuid() PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    age DECIMAL(10),
    version int8 not null,
    book_id UUID not null,
    foreign key (book_id) references books(id)
);
