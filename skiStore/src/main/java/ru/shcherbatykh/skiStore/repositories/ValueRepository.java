package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Value;

import java.util.List;

@Repository
public interface ValueRepository extends CrudRepository<Value, Long> {
    List<Value> findAllByAttribute(Attribute attribute);
}
