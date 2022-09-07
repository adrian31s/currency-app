package com.example.app.demo.currency.Validation;

import com.example.app.demo.currency.code.CurrencyCode;

import java.time.LocalDate;
import java.util.Arrays;

public class CurrencyValidation {


    public static boolean isDataCorrect(String basicCode, String convertedCode, Double value, LocalDate date) {
        if (value == null || value <= 0) {
            return false;
        }

        return isCurrencyDataCorrect(basicCode, convertedCode, date);
    }

    public static boolean isCurrencyDataCorrect(String basicCode, String convertedCode, LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return false;
        }

        if (!(isCurrencyCodeCorrect(basicCode) && isCurrencyCodeCorrect(convertedCode))) {
            return false;
        }
        return true;
    }

    public static boolean isCurrencyCodeCorrect(String currencyCode) {
        return Arrays.stream(CurrencyCode.values()).anyMatch(p -> p.name().equals(currencyCode));
    }

}
