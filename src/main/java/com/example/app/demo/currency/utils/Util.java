package com.example.app.demo.currency.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Util {
    public Double calculateCurrencyValue(Double value, Float currencyRatio) {
        Double result = value * currencyRatio.doubleValue();
        return Math.round(result * 100) / 100d;
    }
}
