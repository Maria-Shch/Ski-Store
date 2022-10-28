package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.models.*;
import ru.shcherbatykh.skiStore.repositories.CartRepository;
import ru.shcherbatykh.skiStore.repositories.InventoryAttributeValueRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final InventoryService inventoryService;
    private final InventoryAttributeValueRepository inventoryAttributeValueRepository;

    public CartService(CartRepository cartRepository, InventoryService inventoryService, InventoryAttributeValueRepository inventoryAttributeValueRepository) {
        this.cartRepository = cartRepository;
        this.inventoryService = inventoryService;
        this.inventoryAttributeValueRepository = inventoryAttributeValueRepository;
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

    @Transactional
    public void deleteCartById(long id){
        cartRepository.deleteById(id);
    }

    public List<CartElement> getCartElementsByUser(User user) {
        List<Cart> carts = getCartsByUser(user);
        List<CartElement> cartElements = new ArrayList<>();

        for(Cart cart: carts){
            CartElement ce = new CartElement(cart, cart.getInventory().getModelOfInventory(), false);
            InventoryAttributeValue iav = inventoryAttributeValueRepository.getFirstByInventory(cart.getInventory());
            if (iav != null){
                ce.setAttribute(iav.getAttribute());
                ce.setValue(iav.getValue());
            }
            cartElements.add(ce);
        }

        return cartElements;
    }
}
