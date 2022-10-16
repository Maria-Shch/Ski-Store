package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.shcherbatykh.skiStore.classes.Filterable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "years")
@NoArgsConstructor
@AllArgsConstructor
public class Year implements Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "period")
    private String name;

    @Override
    public String getNameStr() {
        return getName();
    }
}
