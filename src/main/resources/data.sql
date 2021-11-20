BEGIN TRANSACTION;   -- Ouverture de la transaction

-- ------------------------------------------------------------------
--	SUPPRESION TABLES AVANT CREATION ET INSERTION
-- ------------------------------------------------------------------
-- DROP TABLE IF EXISTS public.user; public.friend; public.account; public.transaction CASCADE;

-- ------------------------------------------------------------------
--	CREATION BDD ET TABLES
-- ------------------------------------------------------------------
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";


CREATE TABLE public.role (
                roleName VARCHAR(5) NOT NULL,
                CONSTRAINT role_pk PRIMARY KEY (roleName)
);


CREATE TABLE public.appUser (
                email VARCHAR(30) NOT NULL,
                password VARCHAR(1000) NOT NULL,
                balance NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
                CONSTRAINT userappli_id PRIMARY KEY (email)
);


CREATE TABLE public.userRole (
                email VARCHAR(30) NOT NULL,
                roleName VARCHAR(5) NOT NULL,
                CONSTRAINT userrole_pk PRIMARY KEY (email, roleName)
);


CREATE TABLE public.friend (
                email1 VARCHAR(30) NOT NULL,
                email2 VARCHAR(30) NOT NULL,
                CONSTRAINT friend_pk PRIMARY KEY (email1, email2)
);


CREATE TABLE public.account (
                accountNumber INTEGER NOT NULL,
                email VARCHAR(30) NOT NULL,
                bank VARCHAR(30) NOT NULL,
                CONSTRAINT account_pk PRIMARY KEY (accountNumber)
);


CREATE TABLE public.transaction (
                id uuid NOT NULL,
                emailUser VARCHAR(30) NOT NULL,
                emailFriend VARCHAR(30),
                accountNumber INTEGER,
                type VARCHAR(12) NOT NULL,
                amount NUMERIC(10,2) DEFAULT 0.0 NOT NULL,
                date TIMESTAMP NOT NULL,
                description VARCHAR(100),
                CONSTRAINT transaction_id PRIMARY KEY (id)
);


ALTER TABLE public.userRole ADD CONSTRAINT role_userrole_fk
FOREIGN KEY (roleName)
REFERENCES public.role (roleName)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.account ADD CONSTRAINT account_userapp_fk
FOREIGN KEY (email)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.friend ADD CONSTRAINT userapp_friend_fk
FOREIGN KEY (email1)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT userapp_transaction_fk
FOREIGN KEY (emailFriend)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.userRole ADD CONSTRAINT userapp_userrole_fk
FOREIGN KEY (email)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.friend ADD CONSTRAINT appuser_friend_fk
FOREIGN KEY (email2)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT appuser_transaction_fk
FOREIGN KEY (emailUser)
REFERENCES public.appUser (email)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

ALTER TABLE public.transaction ADD CONSTRAINT account_transaction_fk
FOREIGN KEY (accountNumber)
REFERENCES public.account (accountNumber)
ON DELETE NO ACTION
ON UPDATE NO ACTION
NOT DEFERRABLE;

-- ------------------------------------------------------------------
--	ROLE
-- ------------------------------------------------------------------
INSERT INTO 
public.role 
(roleName) 
VALUES 
('USER'),
('ADMIN');
-- ------------------------------------------------------------------
--	USER
-- ------------------------------------------------------------------
INSERT INTO 
public.appuser 
(email,password,balance) 
VALUES 
('nicolas.sarkozy@gmail.com','$2a$16$YGBK46FObJOhBfyepXkvFe0d5JE5W4R9KTWpNzz96BfFp.wy1IDge',124755.75),
('francois.hollande@gmail.com','$2a$16$fGlbsbqlFg2cMgCAX7jNyuv7rzQ38yqr8udVwIkAyZ3zmwUVI8nQi',188235.18),
('emmanuel.macron@gmail.com','$2y$10$ZehPSWqwLokGfZVJ//64M.WqzBPs/QzTUVUcbv4VVhG1k41P61xxa',389779.22),
('jacques.chirac@gmail.com','$2a$16$hlGLzSstEe6hVLgQ8uEmcO.E58NOt8rFxccXSZOjG.eT5nBpnQ1xG',99990000.00),
('francois.mitterand@gmail.com','$2a$16$UhIp1wWH0vGhDssDCGA0EuY0MGsmLEp6ZD8HBf6gUxglTddLpz1Fa',129779.22),
('georges.pompidou@gmail.com','$2a$16$g0Z/tgfmfE3ddlZMfCvXXOPZ0FOp7/VDnafPiFJYyyk8JqCS.UYh2',89779.22);

-- ------------------------------------------------------------------
--	USERROLE
-- ------------------------------------------------------------------
INSERT INTO 
public.userrole
(email,roleName) 
VALUES 
('emmanuel.macron@gmail.com','ADMIN'),
('emmanuel.macron@gmail.com','USER'),
('francois.hollande@gmail.com','USER'),
('nicolas.sarkozy@gmail.com','USER'),
('francois.mitterand@gmail.com','USER'),
('jacques.chirac@gmail.com','USER'),
('georges.pompidou@gmail.com','USER');

-- ------------------------------------------------------------------
--	ACCOUNT
-- ------------------------------------------------------------------
INSERT INTO 
public.account
(accountnumber,email,bank) 
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
(email1, email2) 
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
(id, emailuser, emailfriend, accountnumber, type, amount, date, description) 
VALUES 
('b2f6194d-925b-4cbc-8084-da9da0f02d78','emmanuel.macron@gmail.com',NULL,5945210,'deposit',300.50,'2021-08-13 21:18:13.256','Restaurant'),
('e6a083d4-9457-4d67-aad2-45c56f18ff9a','francois.mitterand@gmail.com',NULL,7853129,'withdrawal',600.90,'2021-08-14 08:27:19.457','Tennis'),
('2ba3f3c0-6b07-4b3c-bee7-f96643483864','nicolas.sarkozy@gmail.com','francois.hollande@gmail.com',NULL,'payment',10.5,'2021-08-15 11:04:47.324','Diner'),
('64b2c78b-bc37-4393-8133-56b40a652444','emmanuel.macron@gmail.com',NULL,3578921,'deposit',2789.99,'2021-08-16 12:43:33.125','Vacance'),
('3835380b-3d67-4d5e-94fe-ffad3e43faea','jacques.chirac@gmail.com',NULL,1095542,'withdrawal',870.33,'2021-08-17 16:12:10.666','Golf'),
('df62015a-0c5e-4915-ad90-975a8d12758e','jacques.chirac@gmail.com','francois.mitterand@gmail.com',NULL,'payment',350.55,'2021-08-18 15:34:26.547','Cadeaux');

COMMIT;   -- Validation de la transaction