package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.Year;

import java.util.ArrayList;
import java.util.List;

@Service
public class YearService {
    private final ModelOfInventoryService modelOfInventoryService;

    public YearService(ModelOfInventoryService modelOfInventoryService) {
        this.modelOfInventoryService = modelOfInventoryService;
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
}
