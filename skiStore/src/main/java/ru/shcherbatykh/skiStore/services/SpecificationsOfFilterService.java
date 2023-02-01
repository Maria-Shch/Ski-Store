package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.SpecificationsOfFilter;
import ru.shcherbatykh.skiStore.repositories.SpecificationsOfFilterRepository;

import java.util.List;

@Service
public class SpecificationsOfFilterService {

    private final SpecificationsOfFilterRepository specificationsOfFilterRepository;

    public SpecificationsOfFilterService(SpecificationsOfFilterRepository specificationsOfFilterRepository) {
        this.specificationsOfFilterRepository = specificationsOfFilterRepository;
    }

    @Transactional
    public List<SpecificationsOfFilter> getSpecificationsByModelType(ModelType modelType) {
        return specificationsOfFilterRepository.findAllByModelType(modelType);
    }
}
