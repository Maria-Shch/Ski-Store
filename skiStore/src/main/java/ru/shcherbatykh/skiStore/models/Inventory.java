package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "inventory")
@NoArgsConstructor
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private ModelOfInventory modelOfInventory;

    private int quantity;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "inventory")
    @ToString.Exclude
    private List<InventoryAttributeValue> valuesOfInventoryAttribute = new ArrayList<>();
}
