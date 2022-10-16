package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.repositories.AttributeRepository;

import java.util.List;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;

    public AttributeService(AttributeRepository attributeRepository) {
        this.attributeRepository = attributeRepository;
    }

    @Transactional
    public Attribute getAttributeByName(String name) {
        return attributeRepository.getAttributeByName(name);
    }

    @Transactional
    public List<Attribute> getAttributes() {
        return attributeRepository.findAll();
    }
}
