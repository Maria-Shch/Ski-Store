package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.DynamicInventoryAttribute;
import ru.shcherbatykh.skiStore.models.Inventory;
import ru.shcherbatykh.skiStore.models.InventoryAttributeValue;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.repositories.InventoryRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public DynamicInventoryAttribute getDynamicInventoryAttributeByModel(ModelOfInventory model){
        List<Inventory> presentInventoriesByModel = getPresentInventoriesByModel(model);
        if(presentInventoriesByModel.isEmpty()) return null;

        List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<>();

        for(Inventory inventory : getPresentInventoriesByModel(model)){
            inventoryAttributeValues.addAll(inventory.getValuesOfInventoryAttribute());
        }

        if(inventoryAttributeValues.isEmpty()) return null;

        DynamicInventoryAttribute dynamicInventoryAttribute = new DynamicInventoryAttribute();

        dynamicInventoryAttribute.setAttribute(inventoryAttributeValues.get(0).getAttribute());
        dynamicInventoryAttribute.setValues(inventoryAttributeValues.stream()
                .map(InventoryAttributeValue::getValue)
                .sorted(Comparator.comparing(Value::getName))
                .toList());
        dynamicInventoryAttribute.setUnitOfMeasure(inventoryAttributeValues.get(0).getAttribute().getUnitOfMeasure());
        return dynamicInventoryAttribute;
    }

    @Transactional
    public Inventory getInventoryByModelAndValue(ModelOfInventory model, Value value){
        if (value.getId() == 0){
            return inventoryRepository.getFirstByModelOfInventory(model);
        } else{
            return inventoryRepository.getInventoryByModelOfInventoryAndValue(model.getId(), value.getId());
        }
    }

    @Transactional
    public List<Inventory> getPresentInventoriesByModel(ModelOfInventory modelOfInventory) {
        return inventoryRepository.getAllByModelOfInventoryAndQuantityGreaterThan(modelOfInventory, 0);
    }

    @Transactional
    public boolean checkIsPresentInventoryInStockByModel(ModelOfInventory model) {
        return !getPresentInventoriesByModel(model).isEmpty();
    }

    @Transactional
    public void inventorySale(Inventory inventory, int quantity) {
        int newQuantity = inventory.getQuantity()-quantity;
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
    }
}
