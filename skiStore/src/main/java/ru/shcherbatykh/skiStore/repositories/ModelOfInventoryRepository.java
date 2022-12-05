package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.List;

@Repository
public interface ModelOfInventoryRepository extends CrudRepository<ModelOfInventory, Long> {

    List<ModelOfInventory> findAll();

    ModelOfInventory getModelById(long id);

    List<ModelOfInventory> findAllByModelType(ModelType modelType);

    List<ModelOfInventory> findAll(Specification<ModelOfInventory> specification);
}
