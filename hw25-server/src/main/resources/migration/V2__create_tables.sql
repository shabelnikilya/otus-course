
create table if not exists address(
    id bigint not null primary key,
    street varchar(100)
);

create table if not exists phone (
    id bigint not null primary key,
    number varchar(20),
    client_id bigint references client(id)
);

alter table client add column address_id bigint;