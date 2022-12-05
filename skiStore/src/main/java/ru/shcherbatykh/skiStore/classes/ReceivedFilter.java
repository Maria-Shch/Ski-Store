package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import ru.shcherbatykh.skiStore.models.Attribute;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ReceivedFilter {
    private Long modelType;
    private Integer startPrice;
    private Integer endPrice;
    private List<Long> brandIds;
    private List<Long> yearIds;
    private Map<Attribute, List<Long>> attributes;
}
