package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.AvailabilityStatus;
import ru.shcherbatykh.skiStore.repositories.AvailabilityStatusRepository;

@Service
public class AvailabilityStatusService {
    private final AvailabilityStatusRepository availabilityStatusRepository;

    public AvailabilityStatusService(AvailabilityStatusRepository availabilityStatusRepository) {
        this.availabilityStatusRepository = availabilityStatusRepository;
    }

    @Transactional
    public AvailabilityStatus getAvailableStatus(){
        return availabilityStatusRepository.findByName("Available");
    }
}
