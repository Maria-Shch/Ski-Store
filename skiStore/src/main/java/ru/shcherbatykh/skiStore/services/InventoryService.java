package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.AdminDynamicInventoryAttribute;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.DynamicInventoryAttribute;
import ru.shcherbatykh.skiStore.models.*;
import ru.shcherbatykh.skiStore.repositories.InventoryAttributeValueRepository;
import ru.shcherbatykh.skiStore.repositories.InventoryRepository;

import java.util.*;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryAttributeValueRepository inventoryAttributeValueRepository;

    public InventoryService(InventoryRepository inventoryRepository, InventoryAttributeValueRepository inventoryAttributeValueRepository) {
        this.inventoryRepository = inventoryRepository;
        this.inventoryAttributeValueRepository = inventoryAttributeValueRepository;
    }

    //todo duplicate code
    public DynamicInventoryAttribute getDynamicInventoryAttributeByModel(ModelOfInventory model){
        List<Inventory> presentInventoriesByModel = getPresentInventoriesByModel(model);
        if(presentInventoriesByModel.isEmpty()) return null;

        List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<>();

        for(Inventory inventory : presentInventoriesByModel){
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

    public AdminDynamicInventoryAttribute getAdminDynamicInventoryAttributeByModel(ModelOfInventory model){
        List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<>();

        for(Inventory inventory : model.getInventories()){
            inventoryAttributeValues.addAll(inventory.getValuesOfInventoryAttribute());
        }

        if(inventoryAttributeValues.isEmpty()) return null;

        AdminDynamicInventoryAttribute adminDIA = new AdminDynamicInventoryAttribute();

        adminDIA.setAttribute(inventoryAttributeValues.get(0).getAttribute());
        Map<Value, Integer> values = new TreeMap<>(Comparator.comparing(Value::getName));
        for(InventoryAttributeValue inventoryAttributeValue: inventoryAttributeValues){
            values.put(inventoryAttributeValue.getValue(), inventoryAttributeValue.getInventory().getQuantity());
        }
        adminDIA.setValues(values);
        adminDIA.setUnitOfMeasure(inventoryAttributeValues.get(0).getAttribute().getUnitOfMeasure());
        return adminDIA;
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

    @Transactional
    public void updateQuantity(ModelOfInventory model, AdminDynamicInventoryAttribute aDIA) {
        for(Value value: aDIA.getValues().keySet()){
            Inventory inventory = getInventoryByModelAndValue(model, value);
            inventory.setQuantity(aDIA.getValues().get(value));
        }
    }

    @Transactional
    public void updateQuantity(ModelOfInventory model, int quantity) {
        model.getInventories().forEach(x -> x.setQuantity(quantity));
    }

    public CartElement setAttributeAndValueByInventory(CartElement ce, Inventory inv){
        InventoryAttributeValue iav = inventoryAttributeValueRepository.getFirstByInventory(inv);
        if (iav != null){
            ce.setAttribute(iav.getAttribute());
            ce.setValue(iav.getValue());
        }
        return ce;
    }
}
