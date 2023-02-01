package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import ru.shcherbatykh.skiStore.models.Season;
import ru.shcherbatykh.skiStore.repositories.SeasonRepository;

import java.util.List;

@Service
public class SeasonService {
    private final SeasonRepository seasonRepository;

    public SeasonService(SeasonRepository seasonRepository) {
        this.seasonRepository = seasonRepository;
    }

    public List<Season> findAll(){
        return seasonRepository.findAll();
    }
}
