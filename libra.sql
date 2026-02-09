BEGIN;

-- 1) roles
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_LIBRARIAN'),
('ROLE_USER')
ON CONFLICT (name) DO NOTHING;

-- 2) authors
INSERT INTO authors (full_name) VALUES
('Фёдор Достоевский'),
('Лев Толстой'),
('Александр Пушкин'),
('Михаил Булгаков'),
('Джордж Оруэлл')
ON CONFLICT DO NOTHING;

-- 3) categories
INSERT INTO categories (name) VALUES
('Классика'),
('Фантастика'),
('Роман'),
('Поэзия'),
('Антиутопия')
ON CONFLICT (name) DO NOTHING;

-- 4) publishers
INSERT INTO publishers (name) VALUES
('Эксмо'),
('АСТ'),
('Питер'),
('Азбука'),
('Манн, Иванов и Фербер')
ON CONFLICT (name) DO NOTHING;

-- 5) books
-- ISBN уникальный -> используем ON CONFLICT (isbn)
INSERT INTO books (title, isbn, publication_year, price, author_id, category_id, publisher_id)
VALUES
('Преступление и наказание', '9785170902320', 1866, 599.00,
 (SELECT id FROM authors WHERE full_name='Фёдор Достоевский'),
 (SELECT id FROM categories WHERE name='Роман'),
 (SELECT id FROM publishers WHERE name='Эксмо')
),
('Война и мир', '9785171183667', 1869, 799.00,
 (SELECT id FROM authors WHERE full_name='Лев Толстой'),
 (SELECT id FROM categories WHERE name='Классика'),
 (SELECT id FROM publishers WHERE name='АСТ')
),
('Евгений Онегин', '9785170901002', 1833, 399.00,
 (SELECT id FROM authors WHERE full_name='Александр Пушкин'),
 (SELECT id FROM categories WHERE name='Поэзия'),
 (SELECT id FROM publishers WHERE name='Азбука')
),
('Мастер и Маргарита', '9785170902009', 1967, 549.00,
 (SELECT id FROM authors WHERE full_name='Михаил Булгаков'),
 (SELECT id FROM categories WHERE name='Роман'),
 (SELECT id FROM publishers WHERE name='Питер')
),
('1984', '9785170903006', 1949, 499.00,
 (SELECT id FROM authors WHERE full_name='Джордж Оруэлл'),
 (SELECT id FROM categories WHERE name='Антиутопия'),
 (SELECT id FROM publishers WHERE name='Манн, Иванов и Фербер')
)
ON CONFLICT (isbn) DO NOTHING;

-- 6) book_copies
-- inventory_code делаем уникальным логически, но у тебя может не быть UNIQUE.
-- Поэтому ставим ON CONFLICT DO NOTHING (без колонки)
INSERT INTO book_copies (inventory_code, status, book_id)
VALUES
('INV-0001', 'AVAILABLE', (SELECT id FROM books WHERE isbn='9785170902320')),
('INV-0002', 'AVAILABLE', (SELECT id FROM books WHERE isbn='9785171183667')),
('INV-0003', 'BORROWED',  (SELECT id FROM books WHERE isbn='9785170901002')),
('INV-0004', 'AVAILABLE', (SELECT id FROM books WHERE isbn='9785170902009')),
('INV-0005', 'BORROWED',  (SELECT id FROM books WHERE isbn='9785170903006'))
ON CONFLICT DO NOTHING;

-- 7) members
INSERT INTO members (email, full_name, phone)
VALUES
('admin@libra.local', 'Администратор', '+79990000001'),
('librarian@libra.local', 'Библиотекарь', '+79990000002'),
('user@libra.local', 'Читатель', '+79990000003')
ON CONFLICT (email) DO NOTHING;

-- 8) borrow_records
-- сделаем 2 записи: одна активная, одна возвращенная
INSERT INTO borrow_records (borrowed_at, due_at, returned_at, book_copy_id, member_id)
VALUES
(now() - interval '2 days', now() + interval '12 days', NULL,
 (SELECT id FROM book_copies WHERE inventory_code='INV-0003'),
 (SELECT id FROM members WHERE email='user@libra.local')
),
(now() - interval '20 days', now() - interval '6 days', now() - interval '5 days',
 (SELECT id FROM book_copies WHERE inventory_code='INV-0005'),
 (SELECT id FROM members WHERE email='user@libra.local')
)
ON CONFLICT DO NOTHING;

COMMIT;
SELECT 'roles' t, count(*) FROM roles
UNION ALL SELECT 'authors', count(*) FROM authors
UNION ALL SELECT 'categories', count(*) FROM categories
UNION ALL SELECT 'publishers', count(*) FROM publishers
UNION ALL SELECT 'books', count(*) FROM books
UNION ALL SELECT 'book_copies', count(*) FROM book_copies
UNION ALL SELECT 'members', count(*) FROM members
UNION ALL SELECT 'borrow_records', count(*) FROM borrow_records;

-- AUTHORS
create table authors (
  id bigserial primary key,
  full_name varchar(255) not null
);

-- CATEGORIES
create table categories (
  id bigserial primary key,
  name varchar(120) not null unique
);

-- PUBLISHERS
create table publishers (
  id bigserial primary key,
  name varchar(120) not null unique
);

-- BOOKS
create table books (
  id bigserial primary key,
  title varchar(255) not null,
  isbn varchar(40) not null unique,
  publication_year int not null check (publication_year between 1500 and 2100),
  price numeric(12,2) not null check (price > 0),
  author_id bigint not null references authors(id),
  category_id bigint not null references categories(id),
  publisher_id bigint not null references publishers(id)
);

-- BOOK_COPIES (экземпляры)
create table book_copies (
  id bigserial primary key,
  inventory_code varchar(60) not null unique,
  status varchar(30) not null, -- например: AVAILABLE, BORROWED, LOST
  book_id bigint not null references books(id)
);

-- MEMBERS (читатели)
create table members (
  id bigserial primary key,
  full_name varchar(255) not null,
  email varchar(255) not null unique,
  phone varchar(40)
);

-- BORROW_RECORDS (выдачи)
create table borrow_records (
  id bigserial primary key,
  borrowed_at timestamp not null,
  due_at timestamp not null,
  returned_at timestamp null,
  book_copy_id bigint not null references book_copies(id),
  member_id bigint not null references members(id)
);

-- USERS / ROLES (для авторизации)
create table users (
  id bigserial primary key,
  username varchar(120) not null unique,
  email varchar(255) not null unique,
  password varchar(255) not null,
  enabled boolean not null default true
);

create table roles (
  id bigserial primary key,
  name varchar(60) not null unique -- ROLE_ADMIN, ROLE_LIBRARIAN, ROLE_USER
);

create table user_roles (
  user_id bigint not null references users(id) on delete cascade,
  role_id bigint not null references roles(id) on delete cascade,
  primary key (user_id, role_id)
);

-- Индексы (желательно для скорости)
create index idx_books_author on books(author_id);
create index idx_books_category on books(category_id);
create index idx_books_publisher on books(publisher_id);
create index idx_book_copies_book on book_copies(book_id);
create index idx_borrow_member on borrow_records(member_id);
create index idx_borrow_copy on borrow_records(book_copy_id);
 select * from users