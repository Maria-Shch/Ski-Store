package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.Specifications;

import java.util.List;

@Repository
public interface SpecificationsRepository extends CrudRepository<Specifications, Long> {

    List<Specifications> findAll();

    List<Specifications> findAllByModelType(ModelType modelType);
}
