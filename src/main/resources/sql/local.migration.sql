create table if not exists users
(
    id bigserial primary key,
    vk_id bigserial
);

create unique index if not exists uidx_users_vk_id on users (vk_id);
