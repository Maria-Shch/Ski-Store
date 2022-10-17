package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shcherbatykh.skiStore.classes.Filterable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "values")
@NoArgsConstructor
@AllArgsConstructor
public class Value implements Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "value")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;

    @Override
    public String getNameStr() {
        return getName();
    }
}
