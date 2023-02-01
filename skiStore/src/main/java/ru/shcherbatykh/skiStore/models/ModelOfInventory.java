package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.shcherbatykh.skiStore.utils.CommonUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "models")
@NoArgsConstructor
public class ModelOfInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String title;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_type_id")
    private ModelType modelType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "year_id")
    private Year year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "availability_status_id")
    private AvailabilityStatus availabilityStatus;

    @JoinColumn(name = "image_title")
    private String imageTitle;

    private Double price;
    private Integer discount;

    @Transient
    private Double discountPrice;

    @Transient
    private String priceForPrint;

    @Transient
    private String discountPriceForPrint;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOfInventory")
    @ToString.Exclude
    private List<ModelAttributeValue> valuesOfModelAttribute = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOfInventory")
    @ToString.Exclude
    private List<Inventory> inventories = new ArrayList<>();

    @PostLoad
    public void init() {
        discountPrice = CommonUtils.calculationPrice(price, discount, 1);
        priceForPrint = CommonUtils.decorationPrice(price);
        discountPriceForPrint = CommonUtils.decorationPrice(discountPrice);
    }

    public ModelOfInventory(String title, String description, ModelType modelType, Brand brand, Season season, Year year,
                            AvailabilityStatus availabilityStatus, String imageTitle, Double price, Integer discount) {
        this.title = title;
        this.description = description;
        this.modelType = modelType;
        this.brand = brand;
        this.season = season;
        this.year = year;
        this.availabilityStatus = availabilityStatus;
        this.imageTitle = imageTitle;
        this.price = price;
        this.discount = discount;
    }
}
