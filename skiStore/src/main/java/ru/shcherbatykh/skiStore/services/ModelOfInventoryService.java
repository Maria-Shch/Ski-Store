package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.AvailabilityStatus;
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
    public List<ModelOfInventory> getModelByModelType(ModelType modelType) {
        return modelOfInventoryRepository.getModelsByModelType(modelType);
    }

    @Transactional
    public void updatePrice(long idModel, double newPrice) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        modelOfInventory.setPrice(newPrice);
        modelOfInventoryRepository.save(modelOfInventory);
    }

    @Transactional
    public void updateDiscount(long idModel, int newDiscount) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        modelOfInventory.setDiscount(newDiscount);
        modelOfInventoryRepository.save(modelOfInventory);
    }

    @Transactional
    public void updateDiscount(long idModel, AvailabilityStatus newAvailabilityStatus) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        modelOfInventory.setAvailabilityStatus(newAvailabilityStatus);
        modelOfInventoryRepository.save(modelOfInventory);
    }

}
