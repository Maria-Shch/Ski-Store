package ru.shcherbatykh.skiStore.services;

import lombok.SneakyThrows;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import ru.shcherbatykh.skiStore.classes.ModelCreator;
import ru.shcherbatykh.skiStore.classes.ReceivedFilter;
import ru.shcherbatykh.skiStore.models.*;
import ru.shcherbatykh.skiStore.repositories.ModelOfInventoryRepository;
import ru.shcherbatykh.skiStore.validator.ModelCreatorValidator;

import javax.persistence.criteria.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ModelOfInventoryService {

    @org.springframework.beans.factory.annotation.Value("${skiStore.resources.images.url}")
    private String PATH;

    private final ModelOfInventoryRepository modelOfInventoryRepository;
    private final ModelCreatorValidator modelCreatorValidator;
    private final BrandService brandService;
    private final AvailabilityStatusService availabilityStatusService;
    private final AttributeService attributeService;
    private final InventoryService inventoryService;

    public ModelOfInventoryService(ModelOfInventoryRepository modelOfInventoryRepository,
                                   ModelCreatorValidator modelCreatorValidator, BrandService brandService,
                                   AvailabilityStatusService availabilityStatusService, AttributeService attributeService, InventoryService inventoryService) {
        this.modelOfInventoryRepository = modelOfInventoryRepository;
        this.modelCreatorValidator = modelCreatorValidator;
        this.brandService = brandService;
        this.availabilityStatusService = availabilityStatusService;
        this.attributeService = attributeService;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public List<ModelOfInventory> getModels() {
        return modelOfInventoryRepository.findAll();
    }

    @Transactional
    public ModelOfInventory getModel(long idModel) {
        return modelOfInventoryRepository.getModelById(idModel);
    }

    @Transactional
    public ModelOfInventory findByTitle(String title) {
        return modelOfInventoryRepository.findModelOfInventoryByTitle(title);
    }

    public ModelOfInventory save(ModelOfInventory newModel){
        return modelOfInventoryRepository.save(newModel);
    }

    @Transactional
    public List<ModelOfInventory> getModelsByModelType(ModelType modelType) {
        return modelOfInventoryRepository.findAllByModelType(modelType);
    }

    @Transactional
    public boolean updatePrice(long idModel, String newPrice) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        try{
            Double newPriceDouble = Double.valueOf(newPrice);
            modelOfInventory.setPrice(newPriceDouble);
            modelOfInventoryRepository.save(modelOfInventory);
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }

    @Transactional
    public boolean updateDiscount(long idModel, String newDiscount) {
        ModelOfInventory modelOfInventory = getModel(idModel);
        try {
            Integer newDiscountInteger = Integer.valueOf(newDiscount);
            modelOfInventory.setDiscount(newDiscountInteger);
            modelOfInventoryRepository.save(modelOfInventory);
            return true;
        } catch (ClassCastException e){
            return false;
        }
    }

    public List<ModelOfInventory> filtration(ReceivedFilter receivedFilter) {

        Specification<ModelOfInventory> specification = hasModelType(receivedFilter.getModelType());

        if (receivedFilter.getStartPrice() != null) {
            specification = specification.and(hasStartPrice(Double.valueOf(receivedFilter.getStartPrice())));
        }
        if (receivedFilter.getEndPrice() != null) {
            specification = specification.and(hasEndPrice(Double.valueOf(receivedFilter.getEndPrice())));
        }

        if (!CollectionUtils.isEmpty(receivedFilter.getBrandIds())) {
            specification = specification.and(hasBrands(receivedFilter.getBrandIds()));
        }
        if (!CollectionUtils.isEmpty(receivedFilter.getYearIds())) {
            specification = specification.and(hasYears(receivedFilter.getYearIds()));
        }

        if (!receivedFilter.getAttributes().isEmpty()) {
            for (Map.Entry<Attribute, List<Long>> entry : receivedFilter.getAttributes().entrySet()) {
                if (entry.getKey().isDynamic()) {
                    specification = specification.and(hasDynamicAttributeValues(entry.getKey().getId(), entry.getValue()));
                } else {
                    specification = specification.and(hasStaticAttributeValues(entry.getKey().getId(), entry.getValue()));
                }
            }
        }
        return modelOfInventoryRepository.findAll(specification).stream().toList();
    }

    private Specification<ModelOfInventory> hasModelType(Long modelTypeId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.<ModelType>get("modelType"), modelTypeId);
    }

    private Specification<ModelOfInventory> hasBrands(List<Long> brandIds) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> brandPredicates = new ArrayList<>();
            Path<Brand> brand = root.get("brand");
            for (Long brandId : brandIds) {
                brandPredicates.add(criteriaBuilder.equal(brand, brandId));
            }
            return criteriaBuilder.or(brandPredicates.toArray(new Predicate[0]));
        };
    }

    private Specification<ModelOfInventory> hasYears(List<Long> yearIds) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> yearPredicates = new ArrayList<>();
            Path<Brand> year = root.get("year");
            for (Long yearId : yearIds) {
                yearPredicates.add(criteriaBuilder.equal(year, yearId));
            }
            return criteriaBuilder.or(yearPredicates.toArray(new Predicate[0]));
        };
    }

    private Specification<ModelOfInventory> hasStartPrice(Double startPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(
                getResultPriceExpression(root, criteriaBuilder), startPrice);
    }

    private Specification<ModelOfInventory> hasEndPrice(Double endPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(
                getResultPriceExpression(root, criteriaBuilder), endPrice);
    }

    private Expression<Double> getResultPriceExpression(Root<ModelOfInventory> root, CriteriaBuilder criteriaBuilder) {
        Path<Double> price = root.get("price");
        Path<Integer> discountPercent = root.get("discount");
        Expression<Double> discount = criteriaBuilder.toDouble(criteriaBuilder.quot(
                criteriaBuilder.prod(price, discountPercent), 100));
        return criteriaBuilder.diff(price, discount);
    }

    private Specification<ModelOfInventory> hasStaticAttributeValues(Long attrId, List<Long> listValueIds) {
        return (root, query, criteriaBuilder) -> {
            Join<ModelAttributeValue, ModelOfInventory> modelAttrValue = root.join("valuesOfModelAttribute");
            return hasAttributeValues(modelAttrValue, attrId, listValueIds, criteriaBuilder);
        };
    }

    private Specification<ModelOfInventory> hasDynamicAttributeValues(Long attrId, List<Long> listValueIds) {
        return (root, query, criteriaBuilder) -> {

            Join<Inventory, ModelOfInventory> inventories = root.join("inventories");
            Join<InventoryAttributeValue, Inventory> inventoryAttrValue = inventories.join("valuesOfInventoryAttribute");

            return hasAttributeValues(inventoryAttrValue, attrId, listValueIds, criteriaBuilder);
        };
    }

    private Predicate hasAttributeValues(Join<?, ?> attributeValue,
                                         Long attrId, List<Long> listValueIds,
                                         CriteriaBuilder criteriaBuilder) {
        List<Predicate> valuePredicates = new ArrayList<>();
        Path<Value> value = attributeValue.get("value");
        for (Long listValueId : listValueIds) {
            valuePredicates.add(criteriaBuilder.equal(value, listValueId));
        }

        Path<Attribute> attribute = attributeValue.get("attribute");
        Predicate attributePredicate = criteriaBuilder.equal(attribute, attrId);
        Predicate orValuePredicate = criteriaBuilder.or(valuePredicates.toArray(new Predicate[0]));
        return criteriaBuilder.and(attributePredicate, orValuePredicate);
    }

    @SneakyThrows
    public ModelOfInventory createNewModelOfInventory(ModelCreator modelCreator, BindingResult bindingResult){

        modelCreatorValidator.validate(modelCreator, bindingResult);
        if (bindingResult.hasErrors()) return null;

        StringBuilder path = new StringBuilder(PATH)
            .append(modelCreator.getModelType().getNameEnglish())
            .append("\\");

        StringBuilder imageTitle = new StringBuilder(modelCreator.getTitle())
                .append(".")
                .append(modelCreator.getImage().getOriginalFilename().split("\\.")[1]);

        java.nio.file.Path fileNameAndPath = Paths.get(path.toString(), imageTitle.toString());
        Files.write(fileNameAndPath, modelCreator.getImage().getBytes());

        Brand brand=null;
        if (modelCreator.getBrand() != null) brand = modelCreator.getBrand();
        else {
            brand = brandService.save(new Brand(null, modelCreator.getNewBrand()));
        }

        ModelOfInventory newModel = new ModelOfInventory(modelCreator.getTitle(),
                modelCreator.getDescription(), modelCreator.getModelType(), brand, modelCreator.getSeason(),
                modelCreator.getYear(), availabilityStatusService.getAvailableStatus(), imageTitle.toString(),
                modelCreator.getPrice(), modelCreator.getDiscount());

        ModelOfInventory savedModel = save(newModel);

        if(modelCreator.getAdaptersStaticAttributes()!=null) {
            attributeService.saveStaticAttributesOfNewModel(newModel, modelCreator);
        }

        if(modelCreator.getAdapterDynamicAttribute() == null){
            inventoryService.save(new Inventory(null, newModel, 0));
        }
        else{
            attributeService.saveDynamicAttributesOfNewModel(newModel, modelCreator);
        }

        return savedModel;
    }
}
