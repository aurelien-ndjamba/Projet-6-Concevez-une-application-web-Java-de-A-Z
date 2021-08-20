BEGIN TRANSACTION;   -- Ouverture de la transaction

-- ------------------------------------------------------------------
--	SUPPRESION TABLES AVANT CREATION ET INSERTION
-- ------------------------------------------------------------------
-- DROP TABLE IF EXISTS public.user; public.friend; public.account; public.transaction CASCADE;

-- ------------------------------------------------------------------
--	CREATION BDD ET TABLES
-- ------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE public.user (
                email VARCHAR(30) NOT NULL,
                password VARCHAR(1000) NOT NULL,
                balance NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
                CONSTRAINT userappli_id PRIMARY KEY (email)
);


CREATE TABLE public.friend (
                user_email1 VARCHAR(30) NOT NULL,
                user_email2 VARCHAR(30) NOT NULL,
                CONSTRAINT friend_id PRIMARY KEY (user_email1, user_email2)
);


CREATE TABLE public.account (
                id INTEGER NOT NULL,
                user_email VARCHAR(30) NOT NULL,
                bank VARCHAR(30) NOT NULL,
                CONSTRAINT account_pk PRIMARY KEY (id)
);


CREATE SEQUENCE public.transaction_id_seq;

CREATE TABLE public.transaction (
                id uuid NOT NULL,
                email VARCHAR(30) NOT NULL,
                user_email VARCHAR(30),
                account_id INTEGER DEFAULT NULL,
                type VARCHAR(30) NOT NULL,
                amount NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
                date TIMESTAMP NOT NULL,
                description VARCHAR(100),
                CONSTRAINT transaction_id PRIMARY KEY (id)
);


ALTER SEQUENCE public.transaction_id_seq OWNED BY public.transaction.id;

ALTER TABLE public.account ADD CONSTRAINT account_userapp_fk
FOREIGN KEY (user_email)
REFERENCES public.user (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.friend ADD CONSTRAINT userapp_friend_fk
FOREIGN KEY (user_email1)
REFERENCES public.user (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.friend ADD CONSTRAINT user_friend_fk
FOREIGN KEY (user_email2)
REFERENCES public.user (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT user_transaction_fk1
FOREIGN KEY (email)
REFERENCES public.user (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT user_transaction_fk
FOREIGN KEY (user_email)
REFERENCES public.user (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT account_transaction_fk
FOREIGN KEY (account_id)
REFERENCES public.account (id)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

-- ------------------------------------------------------------------
--	USER
-- ------------------------------------------------------------------
INSERT INTO 
public.user 
(email,password,balance) 
VALUES 
('nicolas.sarkozy@gmail.com','$2y$10$ukfmYcDTgWY8YF1OsCZmSegPGb5PY5IcVmSXQC9pcDyauiVjZu9Im',124755.75),
('francois.hollande@gmail.com','$2y$10$MvvAUdBSfIhJr3W.WvQPHOaIdO7r4aq5eCDXjrdaVhUUYRARyARYq',188235.18),
('emmanuel.macron@gmail.com','$2y$10$UcojoXIGQJfwPaZzOhczGu5azcDLAIigGEZka74iHBxfNa7U1KviG',389779.22),
('jacques.chirac@gmail.com','$2y$10$z97mYO2UTFyUg3WA30QU/e4UNaM1wVjZO0WzPTnxQ2cBb91wSri06',579779.22),
('francois.mitterand@gmail.com','$2y$10$bDzIFaDQX/o0ZY01UsLZ5ugghbyYHgD7PAMXvULgHZ/PHrM1atWry',129779.22),
('georges.pompidou@gmail.com','$2y$10$zDyZ8VhPs.VYU0Y34xJfP.KdHiQV/0/ZYV8oxZXIXp/n5x6ZsvwZO',89779.22);

-- ------------------------------------------------------------------
--	ACCOUNT
-- ------------------------------------------------------------------
INSERT INTO 
public.account
(id,user_email,bank) 
VALUES 
(4691733,'nicolas.sarkozy@gmail.com','BNP PARIBAS'),
(5689045,'georges.pompidou@gmail.com','BANQUE KOLB'),
(9946789,'francois.hollande@gmail.com','CREDIT AGRICOLE'),
(5945210,'emmanuel.macron@gmail.com','BANQUE KOLB'),
(3578921,'emmanuel.macron@gmail.com','BANQUE POPULAIRE'),
(1095542,'jacques.chirac@gmail.com','BNP PARIBAS'),
(7853129,'francois.mitterand@gmail.com','CREDIT LYONNAIS');

-- ------------------------------------------------------------------
--	FRIEND
-- ------------------------------------------------------------------
INSERT INTO 
public.friend
(user_email1,user_email2) 
VALUES 
('emmanuel.macron@gmail.com','francois.hollande@gmail.com'),
('emmanuel.macron@gmail.com','nicolas.sarkozy@gmail.com'),
('emmanuel.macron@gmail.com','francois.mitterand@gmail.com'),
('emmanuel.macron@gmail.com','jacques.chirac@gmail.com'),
('emmanuel.macron@gmail.com','georges.pompidou@gmail.com');

-- ------------------------------------------------------------------
--	TRANSACTION
-- ------------------------------------------------------------------
INSERT INTO 
public.transaction
(id,email,user_email,account_id,type,amount,date,description) 
VALUES 
('b2f6194d-925b-4cbc-8084-da9da0f02d78','emmanuel.macron@gmail.com',NULL,5945210,'deposit',300.50,'2021-08-13 21:18:13.256','Restaurant'),
('e6a083d4-9457-4d67-aad2-45c56f18ff9a','francois.mitterand@gmail.com',NULL,7853129,'withdrawal',600.90,'2021-08-14 08:27:19.457','Tennis'),
('2ba3f3c0-6b07-4b3c-bee7-f96643483864','nicolas.sarkozy@gmail.com','francois.hollande@gmail.com',NULL,'payment',10.5,'2021-08-15 11:04:47.324','Diner'),
('64b2c78b-bc37-4393-8133-56b40a652444','emmanuel.macron@gmail.com',NULL,3578921,'deposit',2789.99,'2021-08-16 12:43:33.125','Vacance'),
('3835380b-3d67-4d5e-94fe-ffad3e43faea','jacques.chirac@gmail.com',NULL,1095542,'withdrawal',870.33,'2021-08-17 16:12:10.666','Golf'),
('df62015a-0c5e-4915-ad90-975a8d12758e','jacques.chirac@gmail.com','francois.mitterand@gmail.com',NULL,'payment',350.55,'2021-08-18 15:34:26.547','Cadeaux');


COMMIT;   -- Validation de la transaction