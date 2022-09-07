package com.example.app.demo.currency.controller;

import com.example.app.demo.currency.service.ICurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class CurrencyController {
    @Autowired
    ICurrencyService currencyService;

    @GetMapping
    public Double convertCurrency(@RequestParam(value = "basicCode") String basicCode,
                                  @RequestParam(value = "convertedCode") String convertedCode,
                                  @RequestParam(value = "value") Double value,
                                  @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date
    ) {
        return currencyService.getConvertedValueFromLocalDB(basicCode, convertedCode, value, date);
    }

}
