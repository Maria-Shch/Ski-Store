INSERT INTO model_types (name) VALUES ('Лыжи беговые');
INSERT INTO model_types (name) VALUES ('Крепления');
INSERT INTO model_types (name) VALUES ('Лыжероллеры');
INSERT INTO model_types (name) VALUES ('Палки лыжные');
INSERT INTO model_types (name) VALUES ('Ботинки');

INSERT INTO availability_statuses (name) VALUES ('Available');
INSERT INTO availability_statuses (name) VALUES ('Unavailable');

INSERT INTO roles (name) VALUES ('Admin');
INSERT INTO roles (name) VALUES ('User');

INSERT INTO attribute_types (name) VALUES ('Text');
INSERT INTO attribute_types (name) VALUES ('Number');
INSERT INTO attribute_types (name) VALUES ('List');

INSERT INTO availability_statuses (name) VALUES ('Available');
INSERT INTO availability_statuses (name) VALUES ('Unavailable');

INSERT INTO brands (name) VALUES ('Rossignol');
INSERT INTO brands (name) VALUES ('Tisa');
INSERT INTO brands (name) VALUES ('Fischer');

INSERT INTO seasons (name) VALUES ('Зима');

INSERT INTO units_of_measure (name) VALUES ('см');

INSERT INTO years (period) VALUES ('2021/2022');

INSERT INTO attributes (name, attribute_type_id, unit_of_measure_id)
VALUES ('Ростовка', 2, 1);
INSERT INTO attributes (name, attribute_type_id, unit_of_measure_id)
VALUES ('Стиль катания', 3);


INSERT INTO inventory_attribute_values (inventory_id, dynamic_attribute_id, value)
SELECT id, dynamic_attribute_id, value
FROM inventory;
