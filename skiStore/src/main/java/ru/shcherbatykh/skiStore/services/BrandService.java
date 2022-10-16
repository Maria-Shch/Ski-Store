package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Brand;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {

    private final ModelOfInventoryService modelOfInventoryService;

    public BrandService(ModelOfInventoryService modelOfInventoryService) {
        this.modelOfInventoryService = modelOfInventoryService;
    }

    @Transactional
    public List<Brand> getBrandsByModelType(ModelType modelType) {
        List<ModelOfInventory> models = modelOfInventoryService.getModelsByModelType(modelType);
        List<Brand> brands = new ArrayList<>();
        for (ModelOfInventory model : models) {
            Brand brand = model.getBrand();
            if (!brands.contains(brand)) brands.add(brand);
        }
        return brands;
    }
}
