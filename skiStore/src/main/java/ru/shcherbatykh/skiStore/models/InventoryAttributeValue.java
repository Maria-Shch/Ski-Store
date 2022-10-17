package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shcherbatykh.skiStore.classes.Filterable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "duplicate_table_inventory_attribute_values")
@NoArgsConstructor
@AllArgsConstructor
public class InventoryAttributeValue implements Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dynamic_attribute_id")
    private Attribute attribute;

    @Column(name = "value")
    private int name;

    @Override
    public String getNameStr() {
        return String.valueOf(getName());
    }
}
