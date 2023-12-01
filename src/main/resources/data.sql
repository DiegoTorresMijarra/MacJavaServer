INSERT INTO POSITION (NAME,SALARY)
VALUES  ('MANAGER',10000.00),
        ('COOKER',1500.00),
        ('CLEANER',1450.00),
        ('WAITER',1550.00),
        ('NOT_ASSIGNED',1000.00);
INSERT INTO workers (DNI,NAME,SURNAME, AGE, PHONE, POSITION_ID)
VALUES ('12345678A', 'John', 'Doe', 25, '123456789', 1),
       ('12345678B', 'Jane', 'Doe', 30, '987654321', 2),
       ('12345678C', 'Jim', 'Smith', 35, '555555555', 3),
       ('12345678D', 'Sarah', 'Johnson', 40, '111111111', 4),
       ('12345678E', 'Mike', 'Brown', 45, '999999999', 4);
insert into workers (uuid,DNI,NAME,SURNAME, AGE, PHONE, POSITION_ID)
values ('550e8400-e29b-41d4-a716-446655440000','12345678p', 'Jane', 'Doe', 30, '987654321', 2);
INSERT INTO CLIENTS (id, dni, name, last_name, age, phone, image, deleted, fecha_cre, fecha_act)
VALUES ('f47ac10b-58cc-4372-a567-0e02b2c3d479', '12121212Q', 'John', 'Doe', 30, '123456789', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
       (UUID(), '12345678A', 'John', 'Doe', 30, '123456789', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
       (UUID(), '87654321B', 'Jane', 'Smith', 25, '987654321', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
       (UUID(), '55555555C', 'Michael', 'Johnson', 40, '555555555', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
       (UUID(), '99999999D', 'Sarah', 'Williams', 35, '999999999', 'https://via.placeholder.com/150', false, '2022-12-12', '2022-12-12'),
       (UUID(), '11111111E', 'David', 'Brown', 28, '111111111', 'https://via.placeholder.com/150', true, '2022-12-12', '2022-12-12');
INSERT INTO PRODUCTS (nombre, precio, stock, gluten, is_deleted, fecha_cre, fecha_act)
VALUES ('Producto1', 10.50, 100, true, false, '2023-01-01', '2023-01-01'),
       ('Producto2', 15.75, 50, false, false, '2023-01-02', '2023-01-02'),
       ('Producto3', 20.00, 75, true, false, '2023-01-03', '2023-01-03'),
       ('Producto4', 8.99, 120, false, true, '2023-01-04', '2023-01-04'),
       ('Producto5', 25.50, 200, true, false, '2023-01-05', '2023-01-05');
INSERT INTO RESTAURANTS ( name, number, is_deleted, creationd,modificationd)
VALUES ( 'res1', 123456789, false, '2023-10-10', '2023-10-10'),
       ( 'res2', 123456778, false, '2023-10-10', '2023-10-10'),
       ( 'res3', 123456877, true, '2023-10-10', '2023-10-10');

-- Datos de ejemplo USUARIOS
-- Contraseña: Admin1
insert into USUARIOS (id,nombre, apellidos, username, email, password)
values ('00000000-0000-0000-0000-000000000000','Admin', 'Admin Admin', 'admin', 'admin@prueba.net',
        '$2a$10$vPaqZvZkz6jhb7U7k/V/v.5vprfNdOnh4sxi/qpPRkYTzPmFlI9p2');

insert into USER_ROLES (user_id, roles)
values ('00000000-0000-0000-0000-000000000000', 'USER');
insert into USER_ROLES (user_id, roles)
values ('00000000-0000-0000-0000-000000000000', 'ADMIN');

-- Contraseña: User1
insert into USUARIOS (id,nombre, apellidos, username, email, password)
values ('00000000-0000-0000-0000-000000000001','User', 'User User', 'user', 'user@prueba.net',
        '$2a$12$RUq2ScW1Kiizu5K4gKoK4OTz80.DWaruhdyfi2lZCB.KeuXTBh0S.');
insert into USER_ROLES (user_id, roles)
values ('00000000-0000-0000-0000-000000000001', 'USER');

-- Contraseña: Test1
insert into USUARIOS (id,nombre, apellidos, username, email, password)
values ('00000000-0000-0000-0000-000000000002','Test', 'Test Test', 'test', 'test@prueba.net',
        '$2a$10$Pd1yyq2NowcsDf4Cpf/ZXObYFkcycswqHAqBndE1wWJvYwRxlb.Pu');
insert into USER_ROLES (user_id, roles)
values ('00000000-0000-0000-0000-000000000002', 'USER');

-- Contraseña: Otro1
insert into USUARIOS (id,nombre, apellidos, username, email, password)
values ('00000000-0000-0000-0000-000000000003','otro', 'Otro Otro', 'otro', 'otro@prueba.net',
        '$2a$12$3Q4.UZbvBMBEvIwwjGEjae/zrIr6S50NusUlBcCNmBd2382eyU0bS');
insert into USER_ROLES (user_id, roles)
values ('00000000-0000-0000-0000-000000000003', 'USER');

