package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import ru.shcherbatykh.skiStore.classes.*;
import ru.shcherbatykh.skiStore.models.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FiltrationService {

    private final SpecificationsService specificationsService;
    private final BrandService brandService;
    private final YearService yearService;
    private final ValueService valueService;
    private final AttributeService attributeService;
    private final ModelOfInventoryService modelOfInventoryService;

    public FiltrationService(SpecificationsService specificationsService, BrandService brandService,
                             YearService yearService, ValueService valueService, AttributeService attributeService,
                             ModelOfInventoryService modelOfInventoryService) {
        this.specificationsService = specificationsService;
        this.brandService = brandService;
        this.yearService = yearService;
        this.valueService = valueService;
        this.attributeService = attributeService;
        this.modelOfInventoryService = modelOfInventoryService;
    }

    public List<FiltrationCategory> getFiltrationParams(ModelType modelType) {

        List<FiltrationCategory> filtrationCategories = new ArrayList<>();
        List<Specifications> specificationsByModelType = specificationsService.getSpecificationsByModelType(modelType);
        List<Attribute> attributes = specificationsByModelType.stream()
                .map(Specifications::getAttribute)
                .toList();

        for (Attribute attribute : attributes) {
            FiltrationCategory fc = new FiltrationCategory();
            fc.setName(attribute.getName());
            List<Value> values = valueService.getPresentValuesByAttribute(attribute);
            fc.setFiltrationParameters(serializationFilterableToFiltrationParameter(values));
            filtrationCategories.add(fc);
        }

        List<Brand> brands = brandService.getBrandsByModelType(modelType);
        List<Year> years = yearService.getYearsByModelType(modelType);

        filtrationCategories.add(new FiltrationCategory("Бренд", serializationFilterableToFiltrationParameter(brands)));
        filtrationCategories.add(new FiltrationCategory("Год", serializationFilterableToFiltrationParameter(years)));

        return filtrationCategories;
    }

    private List<FiltrationParameter> serializationFilterableToFiltrationParameter(List<? extends Filterable> params) {
        List<FiltrationParameter> filtrationParameters = new ArrayList<>();
        for (Filterable param : params) {
            filtrationParameters.add(new FiltrationParameter(param.getId(), param.getName(), false));
        }
        return filtrationParameters;
    }

    public List<ModelOfInventory> filtration(ModelType modelType, Filter filter) {
        List<Long> brands = null;
        List<Long> years = null;
        Map<Attribute, List<Long>> attributes = new HashMap<>();

        for (FiltrationCategory fc : filter.getFiltrationCategories()) {
            if ("Бренд".equals(fc.getName())) {
                brands = getIdListSelectedValues(fc);
            } else if ("Год".equals(fc.getName())) {
                years = getIdListSelectedValues(fc);
            } else {
                List<Long> selectedListValues = getIdListSelectedValues(fc);
                if (!selectedListValues.isEmpty()) {
                    Attribute attribute = attributeService.getAttributeByName(fc.getName());
                    attributes.put(attribute, selectedListValues);
                }
            }
        }
        ReceivedFilter receivedFilter = ReceivedFilter.builder()
                .modelType(modelType.getId())
                .startPrice(filter.getStartPrice())
                .endPrice(filter.getFinalPrice())
                .brandIds(brands)
                .yearIds(years)
                .attributes(attributes)
                .build();

        return modelOfInventoryService.filtration(receivedFilter);
    }

    private List<Long> getIdListSelectedValues(FiltrationCategory fc) {
        return fc.getFiltrationParameters().stream()
                .filter(FiltrationParameter::getSelected)
                .map(FiltrationParameter::getId)
                .toList();
    }
}