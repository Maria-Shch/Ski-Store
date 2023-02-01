package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.InventoryAttributeValue;
import ru.shcherbatykh.skiStore.models.ModelAttributeValue;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.repositories.InventoryAttributeValueRepository;
import ru.shcherbatykh.skiStore.repositories.ModelAttributeValueRepository;
import ru.shcherbatykh.skiStore.repositories.ValueRepository;

import java.util.Comparator;
import java.util.List;

@Service
public class ValueService {
    private final InventoryAttributeValueRepository inventoryAttributeValueRepository;
    private final ModelAttributeValueRepository modelAttributeValueRepository;
    private final ValueRepository valueRepository;

    public ValueService(InventoryAttributeValueRepository inventoryAttributeValueRepository, ModelAttributeValueRepository modelAttributeValueRepository, ValueRepository valueRepository) {
        this.inventoryAttributeValueRepository = inventoryAttributeValueRepository;
        this.modelAttributeValueRepository = modelAttributeValueRepository;
        this.valueRepository = valueRepository;
    }

    @Transactional
    public List<Value> getPresentValuesByAttribute(Attribute attribute) {
        if(attribute.isDynamic()){
            return inventoryAttributeValueRepository.getAllByAttribute(attribute).stream()
                    .map(InventoryAttributeValue::getValue)
                    .distinct()
                    .sorted(Comparator.comparing(x -> Integer.valueOf(x.getName())))
                    .toList();
        }
        else{
            return modelAttributeValueRepository.getAllByAttribute(attribute).stream()
                    .map(ModelAttributeValue::getValue)
                    .distinct()
                    .sorted(Comparator.comparing(Value::getName))
                    .toList();
        }
    }

    public Value findValueByNameAndAttribute(String name, Attribute attribute){
        return valueRepository.findByNameAndAttribute(name, attribute);
    }

    @Transactional
    public Value save(Value value){
        return valueRepository.save(value);
    }
}
