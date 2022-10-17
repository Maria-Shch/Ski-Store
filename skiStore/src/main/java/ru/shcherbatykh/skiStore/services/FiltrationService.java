package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import ru.shcherbatykh.skiStore.classes.Filterable;
import ru.shcherbatykh.skiStore.classes.FiltrationCategory;
import ru.shcherbatykh.skiStore.classes.FiltrationParameter;
import ru.shcherbatykh.skiStore.models.*;

import java.util.*;

@Service
public class FiltrationService {

    private final SpecificationsService specificationsService;
    private final BrandService brandService;
    private final YearService yearService;

    public FiltrationService(SpecificationsService specificationsService, BrandService brandService, YearService yearService) {
        this.specificationsService = specificationsService;
        this.brandService = brandService;
        this.yearService = yearService;
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

            List<Value> values = attribute.getValues();

            if(values.size()!=0){
                fc.setFiltrationParameters(serializationFiterableToFiltrationParameter(values));
            }
            else{
                List<InventoryAttributeValue> inventoryAttributeValues = attribute.getInventoryAttributeValues();
                Set<InventoryAttributeValue> inventoryAttributeValueSet = new TreeSet<>(Comparator.comparing(InventoryAttributeValue::getName));
                inventoryAttributeValueSet.addAll(inventoryAttributeValues);
                fc.setFiltrationParameters(serializationFiterableToFiltrationParameter(inventoryAttributeValueSet.stream().toList()));
            }
            filtrationCategories.add(fc);
        }

        List<Brand> brands = brandService.getBrandsByModelType(modelType);
        List<Year> years = yearService.getYearsByModelType(modelType);

        filtrationCategories.add(new FiltrationCategory("Бренд", serializationFiterableToFiltrationParameter(brands)));
        filtrationCategories.add(new FiltrationCategory("Год", serializationFiterableToFiltrationParameter(years)));

        return filtrationCategories;
    }

    private List<FiltrationParameter> serializationFiterableToFiltrationParameter(List<? extends Filterable> params){
        List<FiltrationParameter> filtrationParameters = new ArrayList<>();
        for (Filterable param : params) {
            filtrationParameters.add(new FiltrationParameter(param.getId(), param.getNameStr(), false));
        }
        return filtrationParameters;
    }
}