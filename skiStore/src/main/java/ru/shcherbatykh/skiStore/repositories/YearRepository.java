package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Year;

import java.util.List;

@Repository
public interface YearRepository extends CrudRepository<Year, Long> {
    List<Year> findAll(Sort sort);
    Year findYearByName(String name);
}
