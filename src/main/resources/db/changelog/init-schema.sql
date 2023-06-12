CREATE TABLE users
(
    username VARCHAR(255) NOT NULL PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    enabled  BOOL         NOT NULL
);

CREATE TABLE authorities
(
    username  VARCHAR(255) NOT NULL,
    authority VARCHAR(255) NOT NULL,
    CONSTRAINT fk_authorities_users FOREIGN KEY (username) REFERENCES users (username)
);

CREATE UNIQUE INDEX ix_auth_username ON authorities (username, authority);

CREATE TABLE vehicle
(
    registration_plate VARCHAR(255) UNIQUE PRIMARY KEY,
    name               VARCHAR(255) NOT NULL
);

CREATE TABLE rfid
(
    number         VARCHAR(255) UNIQUE PRIMARY KEY,
    name           VARCHAR(255) NOT NULL,
    vehicle_number VARCHAR(255),
    FOREIGN KEY (vehicle_number) REFERENCES vehicle (registration_plate)
);

CREATE TABLE customer
(
    id      SERIAL PRIMARY KEY,
    name    VARCHAR(255) NOT NULL,
    rfid_id VARCHAR(255),
    FOREIGN KEY (rfid_id) REFERENCES rfid (number)
);

CREATE TABLE point
(
    serial_number VARCHAR(255) UNIQUE PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    customer_id   INTEGER      NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE port
(
    number              VARCHAR(255) UNIQUE PRIMARY KEY,
    point_serial_number VARCHAR(255) NOT NULL,
    FOREIGN KEY (point_serial_number) REFERENCES point (serial_number)
);

CREATE TABLE session
(
    id          VARCHAR(255) UNIQUE PRIMARY KEY,
    start_date  TIMESTAMP    NOT NULL,
    end_date    TIMESTAMP,
    start_value VARCHAR(255),
    end_value   VARCHAR(255),
    message     VARCHAR(255),
    port_number VARCHAR(255) NOT NULL,
    rfid_number VARCHAR(255),
    FOREIGN KEY (port_number) REFERENCES port (number),
    FOREIGN KEY (rfid_number) REFERENCES rfid (number)
);

