package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Season;

import java.util.List;

@Repository
public interface SeasonRepository extends CrudRepository<Season, Long> {
    List<Season> findAll();
}
