package com.example.app.demo.currency.repository;


import java.time.LocalDate;
import java.util.Optional;

public interface CurrencyRepositoryCustom {
    Optional<Float> getRatioOfCurrency(String basicCurrencyCode, String convertedCurrencyCode, LocalDate date);
}
