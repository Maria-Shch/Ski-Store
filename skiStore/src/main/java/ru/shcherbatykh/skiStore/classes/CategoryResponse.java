package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.shcherbatykh.skiStore.models.ModelOfInventory;
import ru.shcherbatykh.skiStore.models.ModelType;

import java.util.List;

@Getter
@Setter
@Builder
public class CategoryResponse {
    ModelType modelType;
    List<ModelOfInventory> modelsOfInventory;
}
