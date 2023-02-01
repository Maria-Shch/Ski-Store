package ru.shcherbatykh.skiStore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.shcherbatykh.skiStore.classes.Filterable;

import javax.persistence.*;

@Data
@Entity
@Table(name = "brands")
@NoArgsConstructor
@AllArgsConstructor
public class Brand implements Filterable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
}
