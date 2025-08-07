drop schema if exists demo cascade;
create schema demo;
set schema demo;


create table client
(
    id    identity,
    name  varchar not null,
    email varchar not null
);

insert into client (name, email)
values ('Jan Jansen', 'jan.jansen@gmail.com'),
       ('Piet Pietersen', 'piet@pietersen.nl');


create table policy
(
    id        identity,
    name      varchar not null,
    terms_url varchar not null
);

-- TODO rename to insurance?
insert into policy (name, terms_url)
values ('Car Insurance policy', 'https://www.nn.nl/car_insurance_policy.pdf'),
       ('House Insurance policy', 'https://www.nn.nl/house_insurance_policy.pdf');


create table client_policy
(
    id        identity,
    client_id bigint not null,
    policy_id bigint not null,
    foreign key (client_id) references client (id),
    foreign key (policy_id) references policy (id)
);

insert into client_policy (client_id, policy_id)
values (1, 1),
       (2, 2);


create table claim
(
    id               identity,
    client_policy_id bigint  not null,
    date             date    not null,
    description      varchar not null,
    completed        boolean default false,
    foreign key (client_policy_id) references client_policy (id)
);

insert into claim (client_policy_id, date, description)
values (1, '2025-08-01', 'parked my car against a tree');
