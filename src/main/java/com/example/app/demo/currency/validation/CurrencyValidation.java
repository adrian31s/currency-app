package com.example.app.demo.currency.validation;

import com.example.app.demo.currency.code.CurrencyCode;
import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.util.Arrays;

@UtilityClass
public class CurrencyValidation {
    public boolean isCurrencyCorrect(String basicCode, String convertedCode, Double value, LocalDate date) {
        if (value == null || value <= 0) {
            return false;
        }

        return isCurrencyDataCorrect(basicCode, convertedCode, date);
    }

    public boolean isCurrencyDataCorrect(String basicCode, String convertedCode, LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            return false;
        }

        return isCurrencyCodeCorrect(basicCode) && isCurrencyCodeCorrect(convertedCode);
    }

    public boolean isCurrencyCodeCorrect(String currencyCode) {
        return Arrays.stream(CurrencyCode.values()).anyMatch(p -> p.name().equals(currencyCode));
    }

}
