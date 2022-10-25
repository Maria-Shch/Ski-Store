package ru.shcherbatykh.skiStore.classes;

import lombok.Data;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.UnitOfMeasure;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.List;

@Data
public class DynamicInventoryAttribute {
    private Attribute attribute;
    private List<Value> values;
    private UnitOfMeasure unitOfMeasure;
}
