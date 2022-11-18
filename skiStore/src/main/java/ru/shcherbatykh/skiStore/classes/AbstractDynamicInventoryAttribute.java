package ru.shcherbatykh.skiStore.classes;

import lombok.Data;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.UnitOfMeasure;

@Data
public abstract class AbstractDynamicInventoryAttribute {
    protected Attribute attribute;
    protected UnitOfMeasure unitOfMeasure;
}
