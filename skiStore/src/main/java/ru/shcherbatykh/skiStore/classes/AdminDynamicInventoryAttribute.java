package ru.shcherbatykh.skiStore.classes;

import lombok.Data;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.Map;

@Data
public class AdminDynamicInventoryAttribute extends AbstractDynamicInventoryAttribute{
    private Map<Value, Integer> values;
}
