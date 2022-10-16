package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@Builder
public class Filter {
    private Integer startPrice, finalPrice;
    private List<FiltrationCategory> filtrationCategories;
}