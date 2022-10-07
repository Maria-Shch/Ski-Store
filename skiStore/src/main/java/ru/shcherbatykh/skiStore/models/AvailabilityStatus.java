package ru.shcherbatykh.skiStore.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "availability_statuses")
@NoArgsConstructor
public class AvailabilityStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
}
