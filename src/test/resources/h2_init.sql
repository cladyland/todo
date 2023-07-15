drop all objects;

create table if not exists users
(
    id          bigint auto_increment not null,
    username    varchar(20)           not null
        constraint UK_USERNAME unique,
    first_name  varchar(50)           not null,
    last_name   varchar(50)           not null,
    password    varchar(32)           not null,
    create_date timestamp(6),
    last_update timestamp(6),
    primary key (id)
);

create table if not exists tasks
(
    id          bigint auto_increment not null,
    title       varchar(50)           not null,
    description varchar(500),
    priority    varchar(10)           not null,
    status      varchar(15)           not null,
    user_id     bigint                not null,
    create_date timestamp(6),
    last_update timestamp(6),
    primary key (id)
);

create table if not exists tag
(
    id         bigint auto_increment not null,
    title      varchar(15)           not null,
    color      varchar(20)           not null,
    is_default boolean,
    user_id    bigint,
    primary key (id)
);

create table if not exists task_tag
(
    task_id bigint not null,
    tag_id  bigint not null,
    primary key (task_id, tag_id)
);

create table if not exists tasks_comments
(
    id          bigint auto_increment not null,
    contents    varchar(300)          not null,
    task_id     bigint                not null,
    user_id     bigint                not null,
    create_date timestamp(6),
    primary key (id)
);

insert into users (first_name, last_name, username, password)
values ('John', 'Brown', 'test1', 'test1'),
       ('Anna', 'Smith', 'test2', 'test2'),
       ('Lewis', 'Miller', 'test3', 'test3');

insert into tag (title, color, is_default)
values ('Work', '#04ebfb', true),
       ('JavaRush', '#ffa200', true),
       ('Study', '#00ff1e', true),
       ('Sport', '#fffb7a', true),
       ('Design', '#f570ff', true);

insert into tag (title, color, is_default, user_id)
values ('JohnTag', '#20B2AA', false, 1),
       ('JohnTag2', '#20B2AA', false, 1),
       ('AnnaTag', '#20B2AA', false, 2),
       ('LewisTag', '#20B2AA', false, 3);

insert into tasks (title, priority, status, user_id)
values ('Task1', 'MEDIUM', 'PLANNED', 1),
       ('Task2', 'LOW', 'IN_PROGRESS', 1),
       ('Task3', 'HIGH', 'IN_PROGRESS', 3),
       ('Task4', 'MEDIUM', 'DONE', 3),
       ('Task5', 'MEDIUM', 'SUSPENDED', 3)
