package ru.shcherbatykh.skiStore.classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AdapterStaticAttributesForModelCreation {
    private Attribute attribute;
    private List<Value> values;
    private Value selectedValue;
    private String newValue;

    public AdapterStaticAttributesForModelCreation(Attribute attribute, List<Value> values) {
        this.attribute = attribute;
        this.values = values;
    }
}
