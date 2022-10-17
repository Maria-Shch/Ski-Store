package ru.shcherbatykh.skiStore.classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FiltrationParameter {
    private Long id;
    private String name;
    private Boolean selected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FiltrationParameter that = (FiltrationParameter) o;
        return Objects.equals(name, that.name) && Objects.equals(selected, that.selected);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, selected);
    }
}
