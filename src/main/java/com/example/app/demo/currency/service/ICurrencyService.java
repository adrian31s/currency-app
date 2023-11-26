package com.example.app.demo.currency.service;

import java.time.LocalDate;

public interface ICurrencyService {
    Double getConvertedCurrency(String basicCode, String convertedCode, Double value, LocalDate date);
}
