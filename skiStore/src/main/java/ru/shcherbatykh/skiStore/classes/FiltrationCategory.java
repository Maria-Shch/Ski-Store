package ru.shcherbatykh.skiStore.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltrationCategory {
    private String name;
    private List<FiltrationParameter> filtrationParameters;
}
