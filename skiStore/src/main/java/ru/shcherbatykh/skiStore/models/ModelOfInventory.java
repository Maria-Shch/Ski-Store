package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import ru.shcherbatykh.skiStore.utils.CommandUtils;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOfInventory")
    @ToString.Exclude
    private List<ModelAttributeValue> valuesOfModelAttribute = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "modelOfInventory")
    @ToString.Exclude
    private List<Inventory> inventories = new ArrayList<>();

    @PostLoad
    public void init() {
        discountPrice = CommandUtils.calculationPrice(price, discount);
    }
}
