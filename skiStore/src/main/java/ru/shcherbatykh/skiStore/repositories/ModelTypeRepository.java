package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.List;

@Repository
public interface ModelTypeRepository extends CrudRepository<ModelType, Long> {

    List<ModelType> findAll();
}
