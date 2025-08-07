CREATE SCHEMA IF NOT EXISTS public;
SET SCHEMA public;
CREATE TABLE IF NOT EXISTS USERS
(
    name  VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Delete existing rows to prevent duplicates
DELETE FROM public.USERS;

-- Insert the two example rows
INSERT INTO public.USERS (name, email)
VALUES ('Jan', 'Janssen'),
       ('Piet', 'Pietersen');



/*
 tables:
 client -> id, naam, email
 policy -> id, naam, terms_url
 client_policy -> client & policy (ID + twee FKs)
 claim -> claim & policy (ID + twee FKs, datum, description, completed)
 */
