create table if not exists users
(
    id    bigserial primary key,
    vk_id bigserial
);

create unique index if not exists uidx_users_vk_id on users (vk_id);

create table if not exists activities
(
    id       bigserial primary key,
    user_id  bigserial not null references users (id),
    name     varchar   not null,
    date     date      not null,
    duration varchar   not null
);

create index if not exists idx_activities_date on activities using hash(date);
