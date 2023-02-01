package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Brand;

import java.util.List;

@Repository
public interface BrandRepository extends CrudRepository<Brand, Long> {
    List<Brand> findAll(Sort sort);
    Brand findBrandByName(String name);
}
