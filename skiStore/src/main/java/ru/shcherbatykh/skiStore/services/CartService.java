package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.CartResponse;
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

    public Cart getCartById(long id){
        return cartRepository.findById(id).orElse(null);
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

    //todo повторяющийся код
    public Double getResultPriceByCartResponse(CartResponse cartResponse){
        return cartResponse.getCartElements().stream()
                .filter(CartElement::getSelected)
                .mapToDouble(e -> e.getModelOfInventory().getPrice())
                .reduce(Double::sum)
                .orElse(0);
    }

    public Double getResultDiscountPriceByCartResponse(CartResponse cartResponse){
        return cartResponse.getCartElements().stream()
                .filter(CartElement::getSelected)
                .mapToDouble(e -> e.getModelOfInventory().getDiscountPrice())
                .reduce(Double::sum)
                .orElse(0);
    }

    @Transactional
    public void deleteCart(Cart cart){
        cartRepository.deleteById(cart.getId());
    }

    @Transactional
    public void deleteCartById(long idCart){
        cartRepository.deleteById(idCart);
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
