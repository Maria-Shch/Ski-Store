package ru.shcherbatykh.skiStore.classes;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Cart;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.Value;

@Data
@NoArgsConstructor
public class CartElement {
    private Cart cart;
    private ModelOfInventory modelOfInventory;
    private Integer quantity;
    private Attribute attribute;
    private Value value;
    private Boolean selected;
    private Integer quantityInStock;
    private boolean isEnoughInStock;

    public CartElement(Cart cart, ModelOfInventory modelOfInventory, Integer quantity, Boolean selected, Integer quantityInStock, boolean isEnoughInStock) {
        this.cart = cart;
        this.modelOfInventory = modelOfInventory;
        this.quantity = quantity;
        this.selected = selected;
        this.quantityInStock = quantityInStock;
        this.isEnoughInStock = isEnoughInStock;
    }
}
