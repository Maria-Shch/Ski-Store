package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.ModelAttributeValue;

import java.util.List;

@Repository
public interface ModelAttributeValueRepository extends CrudRepository<ModelAttributeValue, Long> {
    List<ModelAttributeValue> getAllByAttribute(Attribute attribute);
}
