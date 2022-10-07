package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Inventory;
import ru.shcherbatykh.skiStore.repositories.InventoryRepository;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public List<Inventory> getInventories() {
        return inventoryRepository.findAll();
    }

    @Transactional
    public Inventory getInventory(long idInventory) {
        return inventoryRepository.getInventoryById(idInventory);
    }

    @Transactional
    public void updateQuantity(long idInventory, int newQuantity) {
        Inventory inventory = getInventory(idInventory);
        inventory.setQuantity(newQuantity);
        inventoryRepository.save(inventory);
    }
}
