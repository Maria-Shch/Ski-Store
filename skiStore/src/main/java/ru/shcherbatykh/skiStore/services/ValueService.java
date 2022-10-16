package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.repositories.ValueRepository;

import java.util.List;

@Service
public class ValueService {
    private final ValueRepository valueRepository;

    public ValueService(ValueRepository valueRepository) {
        this.valueRepository = valueRepository;
    }

    @Transactional
    public List<Value> getValuesByAttribute(Attribute attribute) {
        return valueRepository.findAllByAttribute(attribute);
    }
}
