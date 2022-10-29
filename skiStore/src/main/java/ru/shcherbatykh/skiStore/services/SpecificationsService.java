package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.Specifications;
import ru.shcherbatykh.skiStore.repositories.SpecificationsRepository;

import java.util.List;

@Service
public class SpecificationsService {

    private final SpecificationsRepository specificationsRepository;

    public SpecificationsService(SpecificationsRepository specificationsRepository) {
        this.specificationsRepository = specificationsRepository;
    }

    @Transactional
    public List<Specifications> getSpecificationsByModelType(ModelType modelType) {
        return specificationsRepository.findAllByModelType(modelType);
    }
}
