CREATE TABLE model_types(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE brands(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE years(
id serial PRIMARY KEY,
period varchar(255) NOT NULL
);

CREATE TABLE seasons(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE availability_statuses(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE models(
id serial PRIMARY KEY,
title varchar(255) NOT NULL,
description text,
model_type_id int NOT NULL,
brand_id int NOT NULL,
season_id int NOT NULL,
year_id int NOT NULL,
availability_status_id int NOT NULL,
price money,
discount int,

FOREIGN KEY (model_type_id) REFERENCES model_types (id),
FOREIGN KEY (brand_id) REFERENCES brands (id),
FOREIGN KEY (season_id) REFERENCES seasons (id),
FOREIGN KEY (year_id) REFERENCES years (id),
FOREIGN KEY (availability_status_id) REFERENCES availability_statuses (id)
);

ALTER TABLE models ADD image_title varchar(255);

CREATE TABLE roles(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE cities(
    id serial PRIMARY KEY,
    name varchar(255) NOT NULL
);


CREATE TABLE users(
id serial PRIMARY KEY,
name varchar(255) NOT NULL,
lastname varchar(255) NOT NULL,
email varchar(255) UNIQUE NOT NULL,
password varchar(255) NOT NULL,
registration_date timestamp NOT NULL,
role_id int NOT NULL,

FOREIGN KEY (role_id) REFERENCES roles (id)
);

ALTER TABLE users ADD patronymic varchar(255);
ALTER TABLE users ADD phone_number varchar(20) NOT NULL;
ALTER TABLE users ADD city_id INT;
ALTER TABLE users
    ADD CONSTRAINT fk_cities FOREIGN KEY (city_id) REFERENCES cities (id);
ALTER TABLE users ADD street_name varchar(255) NOT NULL;
ALTER TABLE users ADD house_number int;
ALTER TABLE users ADD flat_number int;
alter table users alter column street_name drop not null;


CREATE TABLE cart(
id serial PRIMARY KEY,
user_id int NOT NULL,
inventory_id int NOT NULL,
quantity int NOT NULL,

FOREIGN KEY (user_id) REFERENCES users (id),
FOREIGN KEY (inventory_id) REFERENCES inventory (id)
);

CREATE TABLE transactions(
id serial PRIMARY KEY,
user_id int NOT NULL,
time timestamp NOT NULL,

FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE TABLE sales(
id serial PRIMARY KEY,
transaction_id int NOT NULL,
inventory_id int NOT NULL,
price money,
discount int,
quantity int NOT NULL,

FOREIGN KEY (transaction_id) REFERENCES transactions (id),
FOREIGN KEY (inventory_id) REFERENCES inventory (id)
);

CREATE TABLE operation_types(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE history(
id serial PRIMARY KEY,
user_id int NOT NULL,
inventory_id int NOT NULL,
operation_type_id int NOT NULL,
quantity int,
time timestamp NOT NULL,

FOREIGN KEY (user_id) REFERENCES users (id),
FOREIGN KEY (inventory_id) REFERENCES inventory (id),
FOREIGN KEY (operation_type_id) REFERENCES operation_types (id)
);


CREATE TABLE attribute_types(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

INSERT INTO attribute_types (name) VALUES ('Text');
INSERT INTO attribute_types (name) VALUES ('Number');
INSERT INTO attribute_types (name) VALUES ('List');

CREATE TABLE units_of_measure(
id serial PRIMARY KEY,
name varchar(255) NOT NULL
);

CREATE TABLE attributes_of_model(
id serial PRIMARY KEY,
name varchar(255) NOT NULL,
attribute_type_id int NOT NULL,
unit_of_measure_id int,

FOREIGN KEY (attribute_type_id) REFERENCES attribute_types (id),
FOREIGN KEY (unit_of_measure_id) REFERENCES units_of_measure (id)
);

CREATE TABLE values(
id serial PRIMARY KEY,
value varchar(255) NOT NULL
);

CREATE TABLE model_attribute_values(
   id serial PRIMARY KEY,
   model_id int NOT NULL,
   attribute_of_model_id int NOT NULL,
   simple_value varchar(255),
   value_id int,

   FOREIGN KEY (model_id) REFERENCES models (id),
   FOREIGN KEY (attribute_of_model_id) REFERENCES attributes (id),
   FOREIGN KEY (value_id) REFERENCES values (id)
);

ALTER TABLE values ADD attribute_of_model_id INT;
ALTER TABLE values
    ADD CONSTRAINT fk_values_attributes FOREIGN KEY (attribute_id) REFERENCES attributes (id);

CREATE TABLE model_attribute_values(
    id serial PRIMARY KEY,
    model_id int NOT NULL,
    attribute_of_model_id int NOT NULL,
    simple_value varchar(255),
    value_id int,

    FOREIGN KEY (model_id) REFERENCES models (id),
    FOREIGN KEY (attribute_of_model_id) REFERENCES attributes (id),
    FOREIGN KEY (value_id) REFERENCES values (id)
);

CREATE TABLE inventory(
    id serial PRIMARY KEY,
    model_id int NOT NULL,
    attribute_of_model_id int NOT NULL,
    value int NOT NULL,
    quantity int,

    FOREIGN KEY (model_id) REFERENCES models (id),
    FOREIGN KEY (attribute_of_model_id) REFERENCES attributes (id)
);

CREATE TABLE inventory_attribute_values(
    id serial PRIMARY KEY,
    inventory_id int NOT NULL,
    dynamic_attribute_id int NOT NULL,
    value int NOT NULL,

    FOREIGN KEY (inventory_id) REFERENCES inventory (id),
    FOREIGN KEY (dynamic_attribute_id) REFERENCES attributes (id)
);

ALTER TABLE inventory_attribute_values ADD value_id INT;
ALTER TABLE inventory_attribute_values
    ADD CONSTRAINT fk_inventory_attribute_values_values FOREIGN KEY (value_id) REFERENCES values (id);

CREATE TABLE specifications(
   id serial PRIMARY KEY,
   model_type_id int,
   attribute_id int,

   FOREIGN KEY (model_type_id) REFERENCES model_types (id),
   FOREIGN KEY (attribute_id) REFERENCES attributes (id)
);

CREATE TABLE duplicate_table_inventory_attribute_values
    AS TABLE inventory_attribute_values;

ALTER TABLE duplicate_table_inventory_attribute_values
    ADD CONSTRAINT fk_duplicate_table_inventory_attribute_values_attributes FOREIGN KEY (dynamic_attribute_id) REFERENCES attributes (id);

ALTER TABLE duplicate_table_inventory_attribute_values
    ADD CONSTRAINT fk_duplicate_table_inventory_attribute_values_inventory FOREIGN KEY (inventory_id) REFERENCES inventory (id);