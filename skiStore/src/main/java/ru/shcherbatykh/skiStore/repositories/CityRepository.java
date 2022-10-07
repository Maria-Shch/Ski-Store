package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.City;

import java.util.List;

@Repository
public interface CityRepository extends CrudRepository<City, Long> {

    List<City> findAll();

    City getCityById(long id);
}
