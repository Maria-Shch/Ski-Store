package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.List;
import java.util.Set;

@Repository
public interface ModelOfInventoryRepository extends CrudRepository<ModelOfInventory, Long> {

    List<ModelOfInventory> findAll();

    ModelOfInventory getModelById(long id);

    ModelOfInventory findModelOfInventoryByTitle(String title);

    List<ModelOfInventory> findAllByModelType(ModelType modelType);

    Set<ModelOfInventory> findAll(Specification<ModelOfInventory> specification);
}
