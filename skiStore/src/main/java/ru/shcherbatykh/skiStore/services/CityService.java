package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.City;
import ru.shcherbatykh.skiStore.repositories.CityRepository;

import java.util.List;

@Service
public class CityService {

    private final CityRepository cityRepository;

    public CityService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Transactional
    public City getCityById(long idCity) {
        return cityRepository.getCityById(idCity);
    }

    @Transactional
    public List<City> getCities() {
        return cityRepository.findAll();
    }
}
