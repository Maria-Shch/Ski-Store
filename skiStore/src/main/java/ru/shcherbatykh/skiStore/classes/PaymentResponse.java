package ru.shcherbatykh.skiStore.classes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.shcherbatykh.skiStore.models.City;
import ru.shcherbatykh.skiStore.models.User;

import java.util.List;

@Setter
@Getter
@Builder
public class PaymentResponse {
    private List<CartElement> selectedCartElements;
    private User user;
    private List<City> cities;
    private String resultPrice;
    private String resultDiscountPrice;
}
