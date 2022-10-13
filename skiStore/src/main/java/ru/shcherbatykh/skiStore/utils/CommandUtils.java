package ru.shcherbatykh.skiStore.utils;

public class CommandUtils {
    public static Double calculationPrice (Double price, int discount){
        return price - price*discount/100;
    }
}
