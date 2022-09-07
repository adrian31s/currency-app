package com.example.app.demo.nbp.service;

import com.example.app.demo.currency.model.Currency;

import java.time.LocalDate;
import java.util.List;

public interface INBPRestApiService {
    List<Currency> getCurrencies(LocalDate date);
}
