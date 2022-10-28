package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class CartResponse {
    private List<CartElement> cartElements;
}
