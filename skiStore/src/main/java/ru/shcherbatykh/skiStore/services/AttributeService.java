package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.repositories.AttributeRepository;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    public Attribute getAttributeByName(String name){
        return attributeRepository.findByName(name);
    }
}
