package ru.shcherbatykh.skiStore.utils;

import java.text.DecimalFormat;
import java.time.LocalDateTime;

public class CommonUtils {
    public static Double calculationPrice (Double price, int discount, int count){
        return (price - price*discount/100)*count;
    }

    public static String decorationPrice(Double price){
        Double newPrice = Math.floor(price);
        DecimalFormat decFormat = new DecimalFormat("###,###");
        return decFormat.format(newPrice);
    }

    public static String convertDateToStringForPrint(LocalDateTime localDateTime){
        int DD = localDateTime.getDayOfMonth();
        int MM = localDateTime.getMonthValue();
        int YY = localDateTime.getYear();

        int HH = localDateTime.getHour();
        int MN = localDateTime.getMinute();
        return String.format("%02d.%02d.%02d %02d:%02d", DD, MM, YY, HH, MN);
    }
}
