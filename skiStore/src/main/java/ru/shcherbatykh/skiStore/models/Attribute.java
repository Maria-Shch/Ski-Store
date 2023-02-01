package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "attributes")
@NoArgsConstructor
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_type_id")
    private AttributeType attributeType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_of_measure_id")
    private UnitOfMeasure unitOfMeasure;

    @Column(name = "is_dynamic")
    private boolean isDynamic;

    @Column(name = "is_addable")
    private boolean isAddable;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute")
    @ToString.Exclude
    private List<Value> values = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "attribute")
    @ToString.Exclude
    private List<InventoryAttributeValue> inventoryAttributeValues = new ArrayList<>();
}
