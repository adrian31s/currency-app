package com.example.app.demo.currency.controller;

import com.example.app.demo.common.exceptions.NotFoundCurrency;
import com.example.app.demo.currency.service.ICurrencyService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
public class CurrencyController {
    private final ICurrencyService currencyService;

    @GetMapping
    public Double convertCurrency(@RequestParam(value = "basicCode") String basicCode,
                                  @RequestParam(value = "convertedCode") String convertedCode,
                                  @RequestParam(value = "value") Double value,
                                  @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            return currencyService.getConvertedCurrency(basicCode, convertedCode, value, date);
        }
        catch (NotFoundCurrency nfc) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Currency not found");
        }
    }

}
