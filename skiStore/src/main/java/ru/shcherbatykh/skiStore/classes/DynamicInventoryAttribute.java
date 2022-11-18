package ru.shcherbatykh.skiStore.classes;

import lombok.Data;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.List;

@Data
public class DynamicInventoryAttribute extends AbstractDynamicInventoryAttribute{
    private List<Value> values;
}
