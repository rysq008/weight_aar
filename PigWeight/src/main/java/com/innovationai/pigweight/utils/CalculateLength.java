package com.innovationai.pigweight.utils;

import java.math.BigDecimal;

public class CalculateLength {

    public static double getLength(double weight) {
        double length;
        if (0 < weight && weight <= 5) {
            length = 6 * weight;
        } else if (5 < weight && weight <= 15) {
            length = 30 + 2 * (weight - 5);
        } else if (15 < weight && weight <= 30) {
            length = 50 + 1.333 * (weight - 15);
        } else if (30 < weight && weight <= 50) {
            length = 70 + (weight - 30);
        } else if (50 < weight && weight <= 80) {
            length = 90 + 0.667 * (weight - 50);
        } else if (80 < weight) {
            double a = (weight - 80) / 20;
            if (a < 1) {
                a = 1;
            }
            length = 110 + (0.667 - Math.log(a) * 0.15) * (weight - 80);
        } else {
            System.out.println("体重必须大于0");
            return -1;

        }
        return new BigDecimal(length).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
