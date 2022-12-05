package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Attribute;

import java.util.List;

@Repository
public interface AttributeRepository extends CrudRepository<Attribute, Long> {

    List<Attribute> findAll();

    Attribute findByName(String name);
}
