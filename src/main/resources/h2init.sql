
CREATE SCHEMA IF NOT EXISTS public;
SET SCHEMA public;
DROP TABLE IF EXISTS clients;
CREATE TABLE IF NOT EXISTS clients
(
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);


-- Insert the two example rows
INSERT INTO clients (name, email)
VALUES ('Jan', 'Janssen'),
       ('Piet', 'Pietersen');



/*
 tables:
 client -> id, naam, email
 policy -> id, naam, terms_url
 client_policy -> client & policy (ID + twee FKs)
 claim -> claim & policy (ID + twee FKs, datum, description, completed)
 */
