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
       ('Piet Pietersen', 'piet@pietersen.nl'),
       ('Kees van Dijk', 'kees.vandijk@gmail.com'),
       ('Anna de Vries', 'anna.devries@example.com'),
       ('Eva Bakker', 'eva.bakker@example.com'); -- New client added


create table policy
(
    id        identity,
    name      varchar not null,
    terms_url varchar not null
);

-- TODO rename to insurance?
insert into policy (name, terms_url)
values ('Car Insurance policy', 'https://www.nn.nl/car_insurance_policy.pdf'),
       ('House Insurance policy', 'https://www.nn.nl/house_insurance_policy.pdf'),
       ('Travel Insurance policy', 'https://www.nn.nl/travel_insurance_policy.pdf');


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
       (2, 2),
       (3, 1),
       (4, 3),
       (5, 2); -- Linking new client Eva Bakker to House Insurance policy


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
values (1, '2025-08-01', 'parked my car against a tree'),
       (1, '2020-02-15', 'rear-ended by another vehicle'),
       (1, '2021-03-10', 'hail damage to car roof'),
       (1, '2022-04-22', 'broken windshield from flying debris'),
       (1, '2023-05-05', 'theft of car stereo'),
       (1, '2024-06-18', 'scratched paint in parking lot'),
       (1, '2020-07-30', 'flat tire from pothole'),
       (1, '2021-08-12', 'engine fire after accident'),
       (1, '2022-09-25', 'side mirror broken by cyclist'),
       (1, '2023-10-14', 'car flooded during heavy rain'),
       (2, '2020-01-20', 'water damage from burst pipe'),
       (2, '2021-02-28', 'stolen bicycle from garage'),
       (2, '2022-03-16', 'fire in kitchen'),
       (2, '2023-04-09', 'roof damaged by storm'),
       (2, '2024-05-21', 'window broken by vandalism'),
       (2, '2020-06-11', 'flooded basement'),
       (2, '2021-07-23', 'electrical short caused damage'),
       (2, '2022-08-05', 'tree fell on house'),
       (2, '2023-09-17', 'lost jewelry during move'),
       (2, '2024-10-29', 'damage from neighbor''s fire'),
       (2, '2020-11-13', 'garage door damaged by car'),
       (3, '2025-09-01', 'car damaged by falling tree branch'),
       (4, '2025-08-18', 'lost luggage during flight'),
       (5, '2025-08-19', 'fake claim for testing'); -- Added claim for new client_policy
