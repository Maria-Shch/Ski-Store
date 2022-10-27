package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.*;
import ru.shcherbatykh.skiStore.repositories.CartRepository;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryService inventoryService;

    public CartService(CartRepository cartRepository, InventoryService inventoryService) {
        this.cartRepository = cartRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public List<Cart> getCartsByUser(User user) {
        return cartRepository.findAllByUser(user);
    }

    @Transactional
    public void addCart(Cart newCart) {
        cartRepository.save(newCart);
    }

    public void addToCart(User user, ModelOfInventory modelOfInventory, Value selectedValue) {
        Inventory inventory = inventoryService.getInventoryByModelAndValue(modelOfInventory, selectedValue);
        addCart(new Cart(null, user, inventory, 1));
    }
}
