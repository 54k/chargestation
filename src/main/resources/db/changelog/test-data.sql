INSERT INTO users (username,
                   password,
                   enabled)
VALUES ('admin', '$2a$10$.RUF0bfZpI.J2IIEzHa9DuCUhCWsqNdZAvgHUG6HUIYfUYxyiNuJC', true);

INSERT INTO authorities (username,
                         authority)
VALUES ('admin', 'admin');

INSERT INTO users (username,
                   password,
                   enabled)
VALUES ('user', '$2a$10$xWjZ.yNx48sjuaxxQubgneSHIHsQJ6yhse8JP.XapRQaEPQKxiUSW', true);

INSERT INTO authorities (username,
                         authority)
VALUES ('user', 'customer');

INSERT INTO vehicle (registration_plate, name)
VALUES ('root001', 'root car');

INSERT INTO rfid (number, name, vehicle_number)
VALUES ('1', 'rfid_1', 'root001');

INSERT INTO customer (id, name, rfid_id)
VALUES (1, 'user', '1');

INSERT INTO point (serial_number, name, customer_id)
VALUES ('1', 'point_1', 1);
INSERT INTO port (number, point_serial_number)
VALUES ('1', '1');

INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1112', 'Nissan R33');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1337', 'Nissan R34');

INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1324', 'Toyota Mark II');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1432', 'Toyota Cresta');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1342', 'Toyota Chaser');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1234', 'Toyota Verossa');

INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1323', 'Honda Accord');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1439', 'Honda Civic');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1340', 'Honda CR-X');
INSERT INTO vehicle (registration_plate, name)
VALUES ('NZ1344', 'Honda CR-Z');


