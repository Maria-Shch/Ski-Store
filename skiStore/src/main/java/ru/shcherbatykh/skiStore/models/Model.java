package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "models")
@NoArgsConstructor
public class Model {
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

    private Double price;
    private Integer discount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model")
    @ToString.Exclude
    private List<ModelAttributeValue> valuesOfModelAttribute = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "model")
    @ToString.Exclude
    private List<Inventory> inventories = new ArrayList<>();
}
