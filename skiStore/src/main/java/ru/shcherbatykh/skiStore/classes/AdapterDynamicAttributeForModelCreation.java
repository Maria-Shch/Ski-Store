package ru.shcherbatykh.skiStore.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Getter
@Setter
@NoArgsConstructor
public class AdapterDynamicAttributeForModelCreation {
    private Attribute attribute;
    private Map<Value, Boolean> selectedValues;

    public AdapterDynamicAttributeForModelCreation(Attribute attribute) {
        this.attribute = attribute;
        List<Value> values = attribute.getValues();
        Map<Value, Boolean> valuesMap = new TreeMap<>(Comparator.comparing(v -> Integer.valueOf(v.getName())));
        for (Value value: values) {
            valuesMap.put(value, false);
        }
        this.selectedValues = valuesMap;
    }
}
