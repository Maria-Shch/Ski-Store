package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Model;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.List;

@Repository
public interface ModelRepository extends CrudRepository<Model, Long> {

    List<Model> findAll();

    Model getModelById(long id);

    Model getModelByModelType(ModelType modelType);
}
