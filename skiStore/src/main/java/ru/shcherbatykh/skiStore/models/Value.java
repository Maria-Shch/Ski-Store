package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shcherbatykh.skiStore.classes.Filterable;

import javax.persistence.*;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value = (Value) o;
        return id == value.id && Objects.equals(name, value.name) && Objects.equals(attribute, value.attribute);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, attribute);
    }
}
