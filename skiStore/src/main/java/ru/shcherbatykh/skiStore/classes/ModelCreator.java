package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import ru.shcherbatykh.skiStore.models.*;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Builder
@Getter
@Setter
public class ModelCreator {
    private String title;
    private String description;
    private ModelType modelType;
    private Brand brand;
    private String newBrand;
    private List<Brand> brands;
    private Season season;
    private List<Season> seasons;
    private Year year;
    private List<Year> years;
    private MultipartFile image;
    private Double price;
    private Integer discount;
    private List<AdapterStaticAttributesForModelCreation> adaptersStaticAttributes;
    private AdapterDynamicAttributeForModelCreation adapterDynamicAttribute;

    public void refreshAdapterDynamicAttribute(){
        for(Map.Entry<Value, Boolean> valueBooleanEntry: getAdapterDynamicAttribute().getSelectedValues().entrySet()){
            if(valueBooleanEntry.getValue() == null){
                valueBooleanEntry.setValue(false);
            }
        }
        Map<Value, Boolean> copyMap = new TreeMap<>(Comparator.comparing(v -> Integer.valueOf(v.getName())));
        copyMap.putAll(getAdapterDynamicAttribute().getSelectedValues());
        getAdapterDynamicAttribute().setSelectedValues(copyMap);
    }
}
