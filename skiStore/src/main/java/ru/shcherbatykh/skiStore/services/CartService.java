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
import java.util.Objects;

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

    public Cart getCartByUserAndInventory(User user, Inventory inventory) {
        return cartRepository.getCartByUserAndInventory(user, inventory);
    }

    @Transactional
    public void addToCart(User user, ModelOfInventory modelOfInventory, Value selectedValue) {
        Inventory inventory = inventoryService.getInventoryByModelAndValue(modelOfInventory, selectedValue);
        Cart currentCart = getCartByUserAndInventory(user, inventory);
        if(currentCart == null){
            addCart(new Cart(null, user, inventory, 1));
        } else {
            addCart(new Cart(currentCart.getId(), currentCart.getUser(), currentCart.getInventory(), currentCart.getQuantity()+1));
        }
    }

    //todo повторяющийся код
    public Double getResultPriceByCartResponse(CartResponse cartResponse){
        return cartResponse.getCartElements().stream()
                .filter(CartElement::getSelected)
                .mapToDouble(e -> e.getModelOfInventory().getPrice()*e.getCart().getQuantity())
                .reduce(Double::sum)
                .orElse(0);
    }

    public Double getResultDiscountPriceByCartResponse(CartResponse cartResponse){
        return cartResponse.getCartElements().stream()
                .filter(CartElement::getSelected)
                .mapToDouble(e -> e.getModelOfInventory().getDiscountPrice()*e.getCart().getQuantity())
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

        for(Cart cart : carts){
            Inventory inv = cart.getInventory();
            CartElement ce = new CartElement(cart, inv.getModelOfInventory(), cart.getQuantity(), true);
            InventoryAttributeValue iav = inventoryAttributeValueRepository.getFirstByInventory(inv);
            if (iav != null){
                ce.setAttribute(iav.getAttribute());
                ce.setValue(iav.getValue());
            }
            cartElements.add(ce);
        }

        return cartElements;
    }

    public void changeQuantity(Cart cart, String operation, int quantity){
        if (Objects.equals(operation, "remove") && cart.getQuantity() == 1){
            deleteCart(cart);
        }
        else{
            switch (operation){
                case "add" -> incrementQuantity(cart, quantity);
                case "remove" -> incrementQuantity(cart, -1*quantity);
                default -> throw new IllegalStateException("Unexpected value: " + operation);
            }
        }
    }

    @Transactional
    public void incrementQuantity(Cart cart, int quantity) {
        int currQuantity = cart.getQuantity();
        cartRepository.save(new Cart(cart.getId(), cart.getUser(), cart.getInventory(), currQuantity + quantity));
    }
}
