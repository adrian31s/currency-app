package com.example.app.demo.currency.controller;

import com.example.app.demo.currency.service.ICurrencyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;

@RestController
public class CurrencyController {
    @Autowired
    ICurrencyService currencyService;


    @ApiOperation(value = "Retun converted currency", response = Double.class)
    @ApiResponses({
            @ApiResponse(description = "Currency code or value or date isnt valid", responseCode = "404"),
            @ApiResponse(description = "Date isnt in correct form", responseCode = "400"),
            @ApiResponse(description = "Successfully converted currency", responseCode = "200")
    })
    @GetMapping
    public Double convertCurrency(@RequestParam(value = "basicCode") String basicCode,
                                  @RequestParam(value = "convertedCode") String convertedCode,
                                  @RequestParam(value = "value") Double value,
                                  @RequestParam(value = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        try {
            Double result = currencyService.getConvertedValueFromLocalDB(basicCode, convertedCode, value, date);
            if (result == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            return result;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
