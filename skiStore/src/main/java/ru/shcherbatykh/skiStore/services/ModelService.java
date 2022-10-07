package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.AvailabilityStatus;
import ru.shcherbatykh.skiStore.models.Model;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.repositories.ModelRepository;
import ru.shcherbatykh.skiStore.repositories.ModelTypeRepository;

import java.util.List;

@Service
public class ModelService {

    private final ModelRepository modelRepository;
    private final ModelTypeRepository modelTypeRepository;

    public ModelService(ModelRepository modelRepository, ModelTypeRepository modelTypeRepository) {
        this.modelRepository = modelRepository;
        this.modelTypeRepository = modelTypeRepository;
    }

    @Transactional
    public List<Model> getModels() {
        return modelRepository.findAll();
    }

    @Transactional
    public List<ModelType> getModelTypes() {
        return modelTypeRepository.findAll();
    }

    @Transactional
    public Model getModel(long idModel) {
        return modelRepository.getModelById(idModel);
    }

    @Transactional
    public Model getModelByModelType(ModelType modelType) {
        return modelRepository.getModelByModelType(modelType);
    }

    @Transactional
    public void updatePrice(long idModel, double newPrice) {
        Model model = getModel(idModel);
        model.setPrice(newPrice);
        modelRepository.save(model);
    }

    @Transactional
    public void updateDiscount(long idModel, int newDiscount) {
        Model model = getModel(idModel);
        model.setDiscount(newDiscount);
        modelRepository.save(model);
    }

    @Transactional
    public void updateDiscount(long idModel, AvailabilityStatus newAvailabilityStatus) {
        Model model = getModel(idModel);
        model.setAvailabilityStatus(newAvailabilityStatus);
        modelRepository.save(model);
    }

}
