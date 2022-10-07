package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "years")
@NoArgsConstructor
public class Year {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String period;
}
