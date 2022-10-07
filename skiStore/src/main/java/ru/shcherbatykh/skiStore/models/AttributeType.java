package ru.shcherbatykh.skiStore.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "attribute_types")
public class AttributeType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
}
