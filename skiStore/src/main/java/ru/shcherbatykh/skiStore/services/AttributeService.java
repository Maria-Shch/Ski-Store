package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import ru.shcherbatykh.skiStore.classes.AdapterDynamicAttributeForModelCreation;
import ru.shcherbatykh.skiStore.classes.AdapterStaticAttributesForModelCreation;
import ru.shcherbatykh.skiStore.classes.ModelCreator;
import ru.shcherbatykh.skiStore.models.*;
import ru.shcherbatykh.skiStore.repositories.AttributeRepository;
import ru.shcherbatykh.skiStore.repositories.InventoryAttributeValueRepository;
import ru.shcherbatykh.skiStore.repositories.ModelAttributeValueRepository;

import java.util.*;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final SpecificationsService specificationsService;
    private final ModelAttributeValueRepository modelAttributeValueRepository;
    private final ValueService valueService;
    private final InventoryService inventoryService;
    private final InventoryAttributeValueRepository inventoryAttributeValueRepository;

    public AttributeService(AttributeRepository attributeRepository, SpecificationsService specificationsService,
                            ModelAttributeValueRepository modelAttributeValueRepository, ValueService valueService,
                            InventoryService inventoryService, InventoryAttributeValueRepository inventoryAttributeValueRepository) {
        this.attributeRepository = attributeRepository;
        this.specificationsService = specificationsService;
        this.modelAttributeValueRepository = modelAttributeValueRepository;
        this.valueService = valueService;
        this.inventoryService = inventoryService;
        this.inventoryAttributeValueRepository = inventoryAttributeValueRepository;
    }

    public Attribute getAttributeByName(String name){
        return attributeRepository.findByName(name);
    }

    public List<AdapterStaticAttributesForModelCreation> getAdaptersStaticAttributeByModelType(ModelType modelType){
        return getAttributesByModelType(modelType).stream()
                .filter(a -> !a.isDynamic())
                .map(a -> new AdapterStaticAttributesForModelCreation(a, a.getValues()))
                .toList();
    }

    public AdapterDynamicAttributeForModelCreation getAdapterDynamicAttributeByModelType(ModelType modelType){
         return getAttributesByModelType(modelType).stream()
                 .filter(a -> a.isDynamic())
                 .map(a -> new AdapterDynamicAttributeForModelCreation(a))
                 .findFirst()
                 .orElse(null);
    }

    public List<Attribute> getAttributesByModelType(ModelType modelType){
        return specificationsService.getSpecificationsByModelType(modelType).stream()
                .map(Specifications::getAttribute)
                .toList();
    }

    public void saveStaticAttributesOfNewModel(ModelOfInventory newModel, ModelCreator modelCreator){
        List<ModelAttributeValue> modelAttributeValues = new ArrayList<>();
        for(AdapterStaticAttributesForModelCreation adapter: modelCreator.getAdaptersStaticAttributes()){
            Attribute currAttribute = adapter.getAttribute();
            if(currAttribute.isAddable() && Objects.equals(currAttribute.getAttributeType().getName(), "List")){
                ModelAttributeValue mav;
                if(adapter.getSelectedValue() != null) {
                    mav = new ModelAttributeValue(null, newModel, currAttribute, null, adapter.getSelectedValue());
                    modelAttributeValues.add(mav);
                }
                else {
                    if (Objects.equals(adapter.getNewValue(), "")) break;
                    else {
                        Value newValue = valueService.save(new Value(null, adapter.getNewValue(), currAttribute));
                        mav = new ModelAttributeValue(null, newModel, currAttribute, null, newValue);
                        modelAttributeValues.add(mav);
                    }
                }
            }
            else if(currAttribute.isAddable() && Objects.equals(currAttribute.getAttributeType().getName(), "Number")){
                if(!Objects.equals(adapter.getNewValue(), "")){
                    modelAttributeValues.add(new ModelAttributeValue(null, newModel, currAttribute, adapter.getNewValue(), null));
                }
            }
            else {
                if(adapter.getSelectedValue() != null) {
                    modelAttributeValues.add(new ModelAttributeValue(null, newModel, currAttribute, null, adapter.getSelectedValue()));
                }
            }
        }
        modelAttributeValueRepository.saveAll(modelAttributeValues);
    }

    public void saveDynamicAttributesOfNewModel(ModelOfInventory newModel, ModelCreator modelCreator){
        AdapterDynamicAttributeForModelCreation adapterDynamicAttribute = modelCreator.getAdapterDynamicAttribute();
        List<Value> selectedDynamicValues = adapterDynamicAttribute.getSelectedValues().entrySet().stream()
                .filter(x -> x.getValue() != null)
                .map(x -> x.getKey())
                .toList();

        for(Value value: selectedDynamicValues){
            Inventory inventory = inventoryService.save(new Inventory(null, newModel, 0));
            inventoryAttributeValueRepository.save(new InventoryAttributeValue(null, inventory, adapterDynamicAttribute.getAttribute(), value));
        }
    }

    public boolean isPresentDynamicInventoryAttribute(ModelType modelType){
        switch (modelType.getName()){
            case "Лыжи беговые", "Палки для беговых лыж", "Ботинки", "Палки для лыжероллеров" -> {
                return true;
            }
            case "Крепления", "Лыжероллеры" ->{
                return false;
            }
        }
        return false;
    }
}
