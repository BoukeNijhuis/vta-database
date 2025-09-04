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
       ('Eva Bakker', 'eva.bakker@example.com'),
       ('Olaf de Boer', 'olaf.deboer@example.com'),
       ('Sophie van den Berg', 'sophie.vandenberg@example.com'),
       ('Mark Sanders', 'mark.sanders@example.com'),
       ('Linda Groot', 'linda.groot@example.com'),
       ('Tom Jansen', 'tom.jansen@example.com'), -- Added five new clients
       ('Quincy Kramer', 'quincy.kramer@example.com'); -- Added 11th client


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
select (select id from client where name = 'Jan Jansen'),
       (select id from policy where name = 'Car Insurance policy')
union all select (select id from client where name = 'Piet Pietersen'),
                 (select id from policy where name = 'House Insurance policy')
union all select (select id from client where name = 'Kees van Dijk'),
                 (select id from policy where name = 'Car Insurance policy')
union all select (select id from client where name = 'Anna de Vries'),
                 (select id from policy where name = 'Travel Insurance policy')
union all select (select id from client where name = 'Eva Bakker'),
                 (select id from policy where name = 'House Insurance policy')
union all select (select id from client where name = 'Olaf de Boer'),
                 (select id from policy where name = 'Car Insurance policy')
union all select (select id from client where name = 'Sophie van den Berg'),
                 (select id from policy where name = 'Travel Insurance policy')
union all select (select id from client where name = 'Mark Sanders'),
                 (select id from policy where name = 'House Insurance policy')
union all select (select id from client where name = 'Linda Groot'),
                 (select id from policy where name = 'Car Insurance policy')
union all select (select id from client where name = 'Tom Jansen'),
                 (select id from policy where name = 'Travel Insurance policy') -- Linked five new clients to existing policies
union all select (select id from client where name = 'Olaf de Boer'),
                 (select id from policy where name = 'Travel Insurance policy')
union all select (select id from client where name = 'Sophie van den Berg'),
                 (select id from policy where name = 'House Insurance policy')
union all select (select id from client where name = 'Linda Groot'),
                 (select id from policy where name = 'Travel Insurance policy');


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
values
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2025-08-01', 'parked my car against a tree'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2020-02-15', 'rear-ended by another vehicle'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2021-03-10', 'hail damage to car roof'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2022-04-22', 'broken windshield from flying debris'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2023-05-05', 'theft of car stereo'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2024-06-18', 'scratched paint in parking lot'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2020-07-30', 'flat tire from pothole'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2021-08-12', 'engine fire after accident'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2022-09-25', 'side mirror broken by cyclist'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Jan Jansen' and p.name='Car Insurance policy'), '2023-10-14', 'car flooded during heavy rain'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2020-01-20', 'water damage from burst pipe'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2021-02-28', 'stolen bicycle from garage'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2022-03-16', 'fire in kitchen'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2023-04-09', 'roof damaged by storm'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2024-05-21', 'window broken by vandalism'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2020-06-11', 'flooded basement'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2021-07-23', 'electrical short caused damage'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2022-08-05', 'tree fell on house'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2023-09-17', 'lost jewelry during move'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2024-10-29', 'damage from neighbor''s fire'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Piet Pietersen' and p.name='House Insurance policy'), '2020-11-13', 'garage door damaged by car'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Kees van Dijk' and p.name='Car Insurance policy'), '2025-09-01', 'car damaged by falling tree branch'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Anna de Vries' and p.name='Travel Insurance policy'), '2025-08-18', 'lost luggage during flight'),
((select cp.id from client_policy cp join client c on cp.client_id=c.id join policy p on cp.policy_id=p.id where c.name='Eva Bakker' and p.name='House Insurance policy'), '2025-08-19', 'fake claim for testing'); -- Added claim for new client_policy
