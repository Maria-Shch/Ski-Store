package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "model_attribute_values")
@NoArgsConstructor
@AllArgsConstructor
public class ModelAttributeValue {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    @ToString.Exclude
    private ModelOfInventory modelOfInventory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "static_attribute_id")
    private Attribute attribute;

    @Column(name = "simple_value")
    private String simpleValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "value_id")
    private Value value;
}
