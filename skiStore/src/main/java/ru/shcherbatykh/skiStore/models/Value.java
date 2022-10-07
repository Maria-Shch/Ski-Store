package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "values")
@NoArgsConstructor
public class Value {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "static_attribute_id")
    private Attribute attribute;
}
