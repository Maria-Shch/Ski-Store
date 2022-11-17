package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.repositories.ModelOfInventoryRepository;

import java.util.List;

@Service
public class ModelOfInventoryService {

    private final ModelOfInventoryRepository modelOfInventoryRepository;

    public ModelOfInventoryService(ModelOfInventoryRepository modelOfInventoryRepository) {
        this.modelOfInventoryRepository = modelOfInventoryRepository;
    }

    @Transactional
    public List<ModelOfInventory> getModels() {
        return modelOfInventoryRepository.findAll();
    }

    @Transactional
    public ModelOfInventory getModel(long idModel) {
        return modelOfInventoryRepository.getModelById(idModel);
    }

    @Transactional
    public List<ModelOfInventory> getModelsByModelType(ModelType modelType) {
        return modelOfInventoryRepository.findAllByModelType(modelType);
    }

    @Transactional
    public boolean updatePrice(long idModel, String newPrice) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        try{
            Double newPriceDouble = Double.valueOf(newPrice);
            modelOfInventory.setPrice(newPriceDouble);
            modelOfInventoryRepository.save(modelOfInventory);
            return true;
        }catch (ClassCastException e){
            return false;
        }
    }

    @Transactional
    public boolean updateDiscount(long idModel, String newDiscount) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        try {
            Integer newDiscountInteger = Integer.valueOf(newDiscount);
            modelOfInventory.setDiscount(newDiscountInteger);
            modelOfInventoryRepository.save(modelOfInventory);
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }
}
