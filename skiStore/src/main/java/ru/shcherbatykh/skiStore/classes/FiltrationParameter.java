package ru.shcherbatykh.skiStore.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltrationParameter {
    private Long id;
    private String name;
    private Boolean selected;
}
