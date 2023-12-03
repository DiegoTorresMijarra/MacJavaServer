SELECT 'CREATE DATABASE macjavaPostgres'
    WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = 'macjavaPostgres');
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
DROP TABLE IF EXISTS "position";
DROP TABLE IF EXISTS "workers";
DROP TABLE IF EXISTS "clients";
DROP TABLE IF EXISTS "products";
DROP TABLE IF EXISTS "restaurants";
DROP SEQUENCE IF EXISTS position_id_seq;
DROP SEQUENCE IF EXISTS products_id_seq;
DROP SEQUENCE IF EXISTS restaurants_id_seq;

CREATE SEQUENCE position_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
-- Crear la tabla position
CREATE TABLE "public"."position" (
                                     "id" bigint DEFAULT nextval('position_id_seq') NOT NULL,
                                     "name" character varying(255),
                                     "salary" double precision,
                                     CONSTRAINT "position_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar datos en la tabla position
INSERT INTO "position" ("id", "name", "salary")
VALUES
    (1, 'MANAGER', 10000.00),
    (2, 'COOKER', 1500.00),
    (3, 'CLEANER', 1450.00),
    (4, 'WAITER', 1550.00),
    (5, 'NOT_ASSIGNED', 1000.00);
-- Crear la tabla workers
CREATE TABLE "public"."workers" (
                                    "id" uuid DEFAULT uuid_generate_v4() NOT NULL,
                                    "dni" character varying(255),
                                    "name" character varying(255),
                                    "surname" character varying(255),
                                    "age" integer,
                                    "phone" character varying(255),
                                    "position_id" bigint,
                                    CONSTRAINT "workers_pkey" PRIMARY KEY ("id"),
                                    CONSTRAINT "fk2fwq10nwymfv7fumctxt9vpgb" FOREIGN KEY ("position_id") REFERENCES "position" ("id") NOT DEFERRABLE
) WITH (oids = false);
-- Insertar datos en la tabla workers
INSERT INTO "workers" ("id", "dni", "name", "surname", "age", "phone", "position_id")
VALUES
    (uuid_generate_v4(), '12345678A', 'John', 'Doe', 25, '123456789', 1),
    (uuid_generate_v4(), '12345678B', 'Jane', 'Doe', 30, '987654321', 2),
    (uuid_generate_v4(), '12345678C', 'Jim', 'Smith', 35, '555555555', 3),
    (uuid_generate_v4(), '12345678D', 'Sarah', 'Johnson', 40, '111111111', 4),
    (uuid_generate_v4(), '12345678E', 'Mike', 'Brown', 45, '999999999', 4);


-- Crear la tabla CLIENTS
CREATE TABLE "public"."clients" (
                                    "id" uuid DEFAULT uuid_generate_v4() NOT NULL,
                                    "dni" character varying(255),
                                    "name" character varying(255),
                                    "last_name" character varying(255),
                                    "age" integer,
                                    "phone" character varying(255),
                                    "image" character varying(255),
                                    "deleted" boolean,
                                    "fecha_cre" timestamp,
                                    "fecha_act" timestamp,
                                    CONSTRAINT "clients_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar datos en la tabla CLIENTS
INSERT INTO "clients" ("id", "dni", "name", "last_name", "age", "phone", "image", "deleted", "fecha_cre", "fecha_act")
VALUES
    (uuid_generate_v4(), '12345678A', 'John', 'Doe', 30, '123456789', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
    (uuid_generate_v4(), '87654321B', 'Jane', 'Smith', 25, '987654321', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
    (uuid_generate_v4(), '55555555C', 'Michael', 'Johnson', 40, '555555555', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
    (uuid_generate_v4(), '99999999D', 'Sarah', 'Williams', 35, '999999999', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
    (uuid_generate_v4(), '11111111E', 'David', 'Brown', 28, '111111111', 'https://via.placeholder.com/150', true, '2022-12-12', '2022-12-12');



CREATE SEQUENCE products_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 6 CACHE 1;
-- Crear la tabla products
CREATE TABLE "public"."products" (
                                     "id" bigint DEFAULT nextval('products_id_seq') NOT NULL,
                                     "nombre" character varying(255),
                                     "precio" double precision,
                                     "stock" integer,
                                     "gluten" boolean,
                                     "is_deleted" boolean,
                                     "fecha_cre" timestamp,
                                     "fecha_act" timestamp,
                                     CONSTRAINT "products_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar la tabla products
INSERT INTO "products" ("id", "nombre", "precio", "stock", "gluten", "is_deleted", "fecha_cre", "fecha_act")
VALUES
    (1, 'Producto1', 10.50, 100, true, false, '2023-01-01', '2023-01-01'),
    (2, 'Producto2', 15.75, 50, false, false, '2023-01-02', '2023-01-02'),
    (3, 'Producto3', 20.00, 75, true, false, '2023-01-03', '2023-01-03'),
    (4, 'Producto4', 8.99, 120, false, true, '2023-01-04', '2023-01-04'),
    (5, 'Producto5', 25.50, 200, true, false, '2023-01-05', '2023-01-05');


CREATE SEQUENCE restaurants_id_seq INCREMENT 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 4 CACHE 1;
-- Crear la tabla restaurants
CREATE TABLE "public"."restaurants" (
                                        "id" bigint DEFAULT nextval('restaurants_id_seq') NOT NULL,
                                        "name" character varying(255),
                                        "number" bigint,
                                        "is_deleted" boolean,
                                        "creationd" timestamp,
                                        "modificationd" timestamp,
                                        CONSTRAINT "restaurants_pkey" PRIMARY KEY ("id")
) WITH (oids = false);
-- Insertar datos en la tabla restaurants
INSERT INTO "restaurants" ("id", "name", "number", "is_deleted", "creationd", "modificationd")
VALUES
    (1, 'Restaurante1', 1, false, '2023-01-01', '2023-01-01'),
    (2, 'Restaurante2', 2, false, '2023-01-02', '2023-01-02'),
    (3, 'Restaurante3', 3, false, '2023-01-03', '2023-01-03');
-- Creación de la tabla usuarios
CREATE TABLE "public"."usuarios"
(
    "is_deleted" boolean DEFAULT false,
    "created_at" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "id" uuid DEFAULT uuid_generate_v4() NOT NULL,
    "updated_at" timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    "apellidos" character varying(255) NOT NULL,
    "email" character varying(255) NOT NULL,
    "nombre" character varying(255) NOT NULL,
    "password" character varying(255) NOT NULL,
    "username" character varying(255) NOT NULL,
    CONSTRAINT "usuarios_email_key" UNIQUE ("email"),
    CONSTRAINT "usuarios_pkey" PRIMARY KEY ("id"),
    CONSTRAINT "usuarios_username_key" UNIQUE ("username")
) WITH (oids = false);
-- Creación de la tabla user_roles
CREATE TABLE "public"."user_roles"
(
    "user_id" uuid NOT NULL,
    "roles" character varying(255)
) WITH (oids = false);

-- Insert usuarios y roles
-- Contraseña: Admin1
INSERT INTO usuarios (is_deleted, created_at, id, updated_at, apellidos, email, nombre, password, username)
VALUES (false, '2023-11-02 11:43:24.724871', '00000000-0000-0000-0000-000000000000', '2023-11-02 11:43:24.724871', 'Admin Admin', 'admin@prueba.net', 'Admin', '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2', 'admin');

-- Asignar roles al administrador
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'USER');
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000000', 'ADMIN');

-- Contraseña: User1
INSERT INTO usuarios (is_deleted, created_at, id, updated_at, apellidos, email, nombre, password, username)
VALUES (false, '2023-11-02 11:43:24.730431', '00000000-0000-0000-0000-000000000001', '2023-11-02 11:43:24.730431', 'User User', 'user@prueba.net', 'User', '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.', 'user');

-- Asignar roles al usuario
INSERT INTO user_roles (user_id, roles)
VALUES ('00000000-0000-0000-0000-000000000001', 'USER');

-- Restricciones de clave externa
ALTER TABLE ONLY "public"."user_roles"
    ADD CONSTRAINT "fk2chxp26bnpqjibydrikgq4t9e" FOREIGN KEY (user_id) REFERENCES usuarios (id) NOT DEFERRABLE;