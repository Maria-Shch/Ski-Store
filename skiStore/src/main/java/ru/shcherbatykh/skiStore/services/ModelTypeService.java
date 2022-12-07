package ru.shcherbatykh.skiStore.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.repositories.ModelTypeRepository;

import java.util.List;

@Service
public class ModelTypeService {

    private final ModelTypeRepository modelTypeRepository;

    public ModelTypeService(ModelTypeRepository modelTypeRepository) {
        this.modelTypeRepository = modelTypeRepository;
    }

    @Transactional
    public List<ModelType> getModelTypes() {
        return modelTypeRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Transactional
    public ModelType getModelTypeByNameEn(String nameEnglish) {
        return modelTypeRepository.findModelTypeByNameEnglish(nameEnglish).orElse(null);
    }
}

