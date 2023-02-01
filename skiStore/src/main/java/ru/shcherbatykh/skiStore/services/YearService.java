package ru.shcherbatykh.skiStore.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.Year;
import ru.shcherbatykh.skiStore.repositories.YearRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class YearService {
    private final ModelOfInventoryService modelOfInventoryService;
    private final YearRepository yearRepository;

    public YearService(ModelOfInventoryService modelOfInventoryService, YearRepository yearRepository) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.yearRepository = yearRepository;
    }

    public List<Year> findAll(){
        return yearRepository.findAll(orderByName());
    }

    private Sort orderByName() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    @Transactional
    public List<Year> getYearsByModelType(ModelType modelType) {
        List<ModelOfInventory> models = modelOfInventoryService.getModelsByModelType(modelType);
        List<Year> years = new ArrayList<>();
        for (ModelOfInventory model : models) {
            Year year = model.getYear();
            if (!years.contains(year)) years.add(year);
        }
        return years;
    }

    @Transactional
    public Year findByName(String name) {
        return yearRepository.findYearByName(name);
    }
}
