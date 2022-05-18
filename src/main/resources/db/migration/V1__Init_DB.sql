create table comments
(
    id      bigserial not null,
    created timestamp,
    updated timestamp,
    message text      not null,
    post_id bigserial,
    user_id bigserial,
    primary key (id)
);

create table images
(
    id          bigserial    not null,
    created     timestamp,
    updated     timestamp,
    image_bytes oid,
    name        varchar(255) not null,
    post_id     bigserial,
    user_id     bigserial,
    primary key (id)
);

create table post_liked_users
(
    post_id    bigserial not null,
    liked_user varchar(255)
);

create table posts
(
    id       bigserial    not null,
    created  timestamp,
    updated  timestamp,
    caption  varchar(255) not null,
    likes    int4,
    location varchar(255),
    title    varchar(255) not null,
    user_id  bigserial,
    primary key (id)
);

create table users
(
    id         bigserial    not null,
    created    timestamp,
    updated    timestamp,
    biography  text,
    email      varchar(255),
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    password   varchar(3000),
    role       varchar(255),
    status     varchar(255),
    username   varchar(255),
    primary key (id)
);

alter table users
    add constraint users_email_fk
        unique (email);

alter table users
    add constraint users_username_fk
        unique (username);

alter table comments
    add constraint comments_posts_fk
        foreign key (post_id) references posts;

alter table comments
    add constraint comments_users_fk
        foreign key (user_id) references users;

alter table post_liked_users
    add constraint post_liked_users_posts_fk
        foreign key (post_id) references posts;

alter table posts
    add constraint posts_users_fk
        foreign key (user_id) references users;
