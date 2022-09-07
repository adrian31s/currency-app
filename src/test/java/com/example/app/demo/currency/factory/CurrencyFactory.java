package com.example.app.demo.currency.factory;

import com.example.app.demo.currency.model.Currency;

import java.time.LocalDate;
import java.util.Random;

public class CurrencyFactory {
    public static Currency createRandomCurrency() {
        Random random = new Random();
        Currency currency = new Currency();
        currency.setCurrencyId(random.nextLong());
        currency.setCurrencyCode("PLN");
        currency.setConvertedCurrencyCode("EUR");
        currency.setCheckedData(LocalDate.now());
        currency.setConvertedValue(random.nextFloat());
        return currency;
    }

    public static Currency createRandomCurrencyWithCodes(String currencyCode, String convertedCurrencyCode) {
        Random random = new Random();
        Currency currency = new Currency();
        currency.setCurrencyId(random.nextLong());
        currency.setCurrencyCode(currencyCode);
        currency.setConvertedCurrencyCode(convertedCurrencyCode);
        currency.setCheckedData(LocalDate.now());
        currency.setConvertedValue(random.nextFloat());
        return currency;
    }



}
