package ru.shcherbatykh.skiStore.services;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Brand;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.repositories.BrandRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> findAll(){
        return brandRepository.findAll(orderByName());
    }

    private Sort orderByName() {
        return Sort.by(Sort.Direction.ASC, "name");
    }

    @Transactional
    public List<Brand> getBrandsByModelType(List<ModelOfInventory> modelsByModelType) {
        List<Brand> brands = new ArrayList<>();
        for (ModelOfInventory model : modelsByModelType) {
            Brand brand = model.getBrand();
            if (!brands.contains(brand)) brands.add(brand);
        }
        return brands;
    }

    @Transactional
    public Brand findByName(String name) {
        return brandRepository.findBrandByName(name);
    }

    @Transactional
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }
}
