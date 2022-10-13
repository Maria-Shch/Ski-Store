package ru.shcherbatykh.skiStore.utils;

import java.text.DecimalFormat;

public class CommonUtils {
    public static Double calculationPrice (Double price, int discount){
        return price - price*discount/100;
    }

    public static String decorationPrice(Double price){
        Double newPrice = Math.floor(price);
        DecimalFormat decFormat = new DecimalFormat("###,###");
        return decFormat.format(newPrice);
    }
}
