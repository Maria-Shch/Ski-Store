package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "units_of_measure")
@NoArgsConstructor
public class UnitOfMeasure {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
}
