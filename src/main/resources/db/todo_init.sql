create table if not exists users
(
    id          bigserial   not null,
    username    varchar(20) not null
        constraint UK_USERNAME unique,
    first_name  varchar(50) not null,
    last_name   varchar(50) not null,
    password    varchar(32) not null,
    create_date timestamp(6),
    last_update timestamp(6),
    primary key (id)
);

create table if not exists tasks
(
    id          bigserial    not null,
    title       varchar(255) not null,
    description text,
    priority    varchar(255) not null,
    status      varchar(255) not null,
    user_id     bigint       not null,
    create_date timestamp(6),
    last_update timestamp(6),
    primary key (id),
    constraint FK_TASKS_USER_ID foreign key (user_id) references users (id) on delete cascade
);

create table if not exists tag
(
    id         bigserial    not null,
    title      varchar(20)  not null,
    color      varchar(255) not null,
    is_default boolean,
    user_id    bigint,
    primary key (id),
    constraint FK_TAG_USER_ID foreign key (user_id) references users (id) on delete cascade
);

create table if not exists task_tag
(
    task_id bigint not null,
    tag_id  bigint not null,
    primary key (task_id, tag_id),
    constraint FK_TASK_TAG_TAG_ID foreign key (tag_id) references tag (id) on delete cascade,
    constraint FK_TASK_TAG_TASK_ID foreign key (task_id) references tasks (id) on delete cascade
);

create table if not exists tasks_comments
(
    id          bigserial    not null,
    contents    varchar(255) not null,
    task_id     bigint       not null,
    user_id     bigint       not null,
    create_date timestamp(6),
    primary key (id),
    constraint FK_TASK_COMMENTS_USER_ID foreign key (user_id) references users (id) on delete cascade,
    constraint FK_TASK_COMMENTS_TASK_ID foreign key (task_id) references tasks (id) on delete cascade
);

insert into tag (title, color, is_default)
values ('Work', '#00b3ff', true),
       ('JavaRush', '#ffa200', true),
       ('Study', '#00ff1e', true),
       ('Sport', '#fffb7a', true),
       ('Design', '#f570ff', true)
