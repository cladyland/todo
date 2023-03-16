-- Uncomment the line below if you want to remove an existing schema
-- drop schema if exists todo;
create schema todo;
create table todo.users
(
    id          bigint      not null auto_increment,
    username    varchar(20) not null unique,
    first_name  varchar(50) not null,
    last_name   varchar(50) not null,
    password    varchar(32) not null,
    create_date datetime(6),
    last_update datetime(6),
    primary key (id)
);
create table todo.tasks
(
    id          bigint       not null auto_increment,
    title       varchar(255) not null,
    description text,
    priority    varchar(255) not null,
    status      varchar(255) not null,
    user_id     bigint       not null,
    create_date datetime(6),
    last_update datetime(6),
    primary key (id),
    foreign key (user_id) references users (id)
);
create table todo.tag
(
    id         bigint       not null auto_increment,
    title      varchar(20)  not null,
    color      varchar(255) not null,
    is_default bit,
    user_id    bigint,
    primary key (id),
    foreign key (user_id) references users (id)
);
create table todo.task_tag
(
    task_id bigint not null,
    tag_id  bigint not null,
    primary key (task_id, tag_id),
    foreign key (tag_id) references tag (id),
    foreign key (task_id) references tasks (id)
);
create table todo.tasks_comments
(
    id          bigint       not null auto_increment,
    contents    varchar(255) not null,
    task_id     bigint       not null,
    user_id     bigint       not null,
    create_date datetime(6),
    primary key (id),
    foreign key (user_id) references users (id),
    foreign key (task_id) references tasks (id)
);
insert into todo.tag (title, color, is_default)
values ('Work', '#00b3ff', true),
       ('JavaRush', '#ffa200', true),
       ('Study', '#00ff1e', true),
       ('Sport', '#fffb7a', true),
       ('Design', '#f570ff', true)
