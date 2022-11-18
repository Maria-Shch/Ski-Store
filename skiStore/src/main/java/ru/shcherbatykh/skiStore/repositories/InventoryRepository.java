package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Inventory;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;

import java.util.List;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    List<Inventory> findAll();

    List<Inventory> getAllByModelOfInventoryAndQuantityGreaterThan(ModelOfInventory model, int quantity);

    Inventory getFirstByModelOfInventory(ModelOfInventory model);

    @Query(value = """
            SELECT inventory.id, inventory.model_id, inventory.quantity FROM inventory
            LEFT JOIN models ON inventory.model_id = models.id
            LEFT JOIN inventory_attribute_values ON inventory.id = inventory_attribute_values.inventory_id
            LEFT JOIN values ON inventory_attribute_values.value_id = values.id
            WHERE model_id=:modelId AND value_id=:valueId""",
            nativeQuery = true)
    Inventory getInventoryByModelOfInventoryAndValue(@Param("modelId") long modelId,
                                                     @Param("valueId") long valueId);

    List<Inventory> findAllByModelOfInventory(ModelOfInventory modelOfInventory);
}