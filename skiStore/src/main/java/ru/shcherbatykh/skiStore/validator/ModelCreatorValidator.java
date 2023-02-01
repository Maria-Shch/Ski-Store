package ru.shcherbatykh.skiStore.validator;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.shcherbatykh.skiStore.classes.AdapterDynamicAttributeForModelCreation;
import ru.shcherbatykh.skiStore.classes.AdapterStaticAttributesForModelCreation;
import ru.shcherbatykh.skiStore.classes.ModelCreator;
import ru.shcherbatykh.skiStore.models.Attribute;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.services.BrandService;
import ru.shcherbatykh.skiStore.services.ModelOfInventoryService;
import ru.shcherbatykh.skiStore.services.ValueService;

import java.util.Objects;

@Component
public class ModelCreatorValidator implements Validator {

    private final ModelOfInventoryService modelOfInventoryService;
    private final ValueService valueService;
    private final BrandService brandService;

    @Lazy
    public ModelCreatorValidator(ModelOfInventoryService modelOfInventoryService, ValueService valueService,
                                  BrandService brandService) {
        this.modelOfInventoryService = modelOfInventoryService;
        this.valueService = valueService;
        this.brandService = brandService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ModelCreator modelCreator = (ModelCreator) o;
        String imageFormat = modelCreator.getImage().getOriginalFilename().split("\\.")[1];

        if (modelOfInventoryService.findByTitle(modelCreator.getTitle()) != null) {
            errors.rejectValue("title", "Duplicate.modelCreator.title");
        }

        if (modelCreator.getBrand() == null && Objects.equals(modelCreator.getNewBrand(), "") ||
            modelCreator.getBrand() != null && !Objects.equals(modelCreator.getNewBrand(), "")) {
            errors.rejectValue("brand", "Conflict.modelCreator.field");
        }

        if (!Objects.equals(modelCreator.getNewBrand(), "") && brandService.findByName(modelCreator.getNewBrand()) != null) {
            errors.rejectValue("brand", "Duplicate.modelCreator.brand");
        }

        if (modelCreator.getYear() == null) {
            errors.rejectValue("year", "NotSelected.modelCreator.year");
        }

        if (modelCreator.getSeason() == null) {
            errors.rejectValue("season", "NotSelected.modelCreator.season");
        }

        if(!Objects.equals(imageFormat, "png") &&
            !Objects.equals(imageFormat, "jpg") &&
            !Objects.equals(imageFormat, "jpeg")){
                errors.rejectValue("image", "WrongFormat.modelCreator.image");
        }

        if ( modelCreator.getAdaptersStaticAttributes() != null){
            for (int i = 0; i < modelCreator.getAdaptersStaticAttributes().size(); i++) {
                AdapterStaticAttributesForModelCreation attributeAdapter = modelCreator.getAdaptersStaticAttributes().get(i);
                Attribute attribute = attributeAdapter.getAttribute();
                String newValue = attributeAdapter.getNewValue();

                if (attribute.isAddable() &&
                        attributeAdapter.getSelectedValue() != null && !Objects.equals(newValue, "")){
                    errors.rejectValue("adaptersStaticAttributes[" + i + "]", "Conflict.modelCreator.field");
                }

                if (attribute.isAddable() &&
                        Objects.equals(attribute.getAttributeType().getName(), "Number") &&
                        !Objects.equals(newValue, "")){
                    try {
                        int temp = Integer.parseInt(newValue);
                        if(temp <= 0) errors.rejectValue("adaptersStaticAttributes[" + i + "]", "FailedCastToNumber.modelCreator.attribute");
                    }
                    catch (NumberFormatException nfe) {
                        errors.rejectValue("adaptersStaticAttributes[" + i + "]", "FailedCastToNumber.modelCreator.attribute");
                    }
                }

                if (attribute.isAddable() && !Objects.equals(newValue, "")){
                    if (valueService.findValueByNameAndAttribute(newValue, attribute) != null){
                        errors.rejectValue("adaptersStaticAttributes[" + i + "]", "Duplicate.modelCreator.attribute");
                    }
                }
            }
        }

        AdapterDynamicAttributeForModelCreation adapterDynamicAttribute = modelCreator.getAdapterDynamicAttribute();
        if (adapterDynamicAttribute!=null){
            if(adapterDynamicAttribute.getSelectedValues().values()
                    .stream()
                    .allMatch(Objects::isNull)){
                errors.rejectValue("adapterDynamicAttribute", "NotSelected.modelCreator.dynamicAttribute");
            }
        }
    }
}
