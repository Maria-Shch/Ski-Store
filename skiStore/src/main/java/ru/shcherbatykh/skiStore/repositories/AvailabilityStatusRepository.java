package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.shcherbatykh.skiStore.models.AvailabilityStatus;

public interface AvailabilityStatusRepository extends CrudRepository<AvailabilityStatus, Long> {
    AvailabilityStatus findByName(String name);
}
