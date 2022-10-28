SELECT models.title,
       models.description,
       model_types.name as type,
       brands.name as brand,
       seasons.name as season,
       years.period as year,
       availability_statuses.name as availability,
       attributes.name as attribute,
       value,
       uom.name,
       price,
       discount,
       (price - (price*discount/100)) as result_cost,
       quantity
FROM inventory
    LEFT JOIN models ON inventory.model_id = models.id
    LEFT JOIN brands ON models.brand_id = brands.id
    LEFT JOIN model_types ON models.model_type_id = model_types.id
    LEFT JOIN seasons ON models.season_id = seasons.id
    LEFT JOIN years ON models.year_id = years.id
    LEFT JOIN availability_statuses ON models.availability_status_id = availability_statuses.id
    LEFT JOIN attributes ON inventory.dynamic_attribute_id = attributes.id
    LEFT JOIN units_of_measure uom on attributes.unit_of_measure_id = uom.id;

SELECT
    models.id,
    models.title,
    model_types.name as type,
    attributes.name as attribute,
    values.value
FROM model_attribute_values
     LEFT JOIN models
               ON model_attribute_values.model_id = models.id
     LEFT JOIN model_types
               ON models.model_type_id = model_types.id
     LEFT JOIN attributes
               ON model_attribute_values.static_attribute_id = attributes.id
     LEFT JOIN attribute_types
               ON attributes.attribute_type_id=attribute_types.id
     LEFT JOIN units_of_measure
               ON attributes.unit_of_measure_id=units_of_measure.id
     LEFT JOIN values
               ON model_attribute_values.value_id = values.id
WHERE model_id=1;

SELECT value
FROM values
WHERE static_attribute_id=3;

SELECT T.* FROM models m
JOIN model_attribute_values T On m.id=T.model_id
WHERE m.id=35;

SELECT * FROM inventory
  LEFT JOIN models ON inventory.model_id = models.id
  LEFT JOIN inventory_attribute_values ON inventory.id = inventory_attribute_values.inventory_id;

SELECT inventory.id, model_id, inventory.quantity FROM inventory
LEFT JOIN models ON inventory.model_id = models.id
LEFT JOIN inventory_attribute_values ON inventory.id = inventory_attribute_values.inventory_id
LEFT JOIN values ON inventory_attribute_values.value_id = values.id
WHERE model_id=31 AND value_id=154;