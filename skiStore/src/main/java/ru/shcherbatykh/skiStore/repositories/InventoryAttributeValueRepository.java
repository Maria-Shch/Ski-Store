package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Inventory;
import ru.shcherbatykh.skiStore.models.InventoryAttributeValue;

import java.util.List;

@Repository
public interface InventoryAttributeValueRepository extends CrudRepository<InventoryAttributeValue, Long> {

    List<InventoryAttributeValue> getAllByAttribute(Attribute attribute);

    InventoryAttributeValue getFirstByInventory(Inventory inventory);
}
