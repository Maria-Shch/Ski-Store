package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;

@Getter
@Setter
@Builder
public class ModelOfInventoryResponse {
    private ModelOfInventory modelOfInventory;
    private DynamicInventoryAttribute dynamicInventoryAttribute;
    private boolean isPresentDynamicInventoryAttribute;
    private boolean isPresentInStock;
}
