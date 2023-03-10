--liquibase formatted sql

--changeset pmart5a:1
create table if not exists users
(
    id bigint auto_increment primary key not null,
    login varchar(255) not null unique,
    password varchar(255) not null
    );

--changeset pmart5a:2
create table if not exists files
(
    id bigint auto_increment primary key not null,
    file_name varchar(255) not null,
    file_size bigint not null,
    file_type varchar(255) not null,
    file_date_update datetime(6) not null,
    file_byte longblob not null,
    user_id bigint null,
    foreign key (user_id) references users (id),
    index (file_name)
);