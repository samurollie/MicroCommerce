create table public.user (
    id bigserial primary key,
    name varchar(255) not null,
    email varchar(255) not null unique,
    password varchar(255) not null,
    created_at timestamp default current_timestamp
);