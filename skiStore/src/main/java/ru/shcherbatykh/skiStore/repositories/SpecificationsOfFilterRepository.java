package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.ModelType;
import ru.shcherbatykh.skiStore.models.SpecificationsOfFilter;

import java.util.List;

@Repository
public interface SpecificationsOfFilterRepository extends CrudRepository<SpecificationsOfFilter, Long> {

    List<SpecificationsOfFilter> findAll();

    List<SpecificationsOfFilter> findAllByModelType(ModelType modelType);
}
