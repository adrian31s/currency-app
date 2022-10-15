package com.example.app.demo.currency.service;

import com.example.app.demo.currency.Validation.CurrencyValidation;
import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.model.Currency;
import com.example.app.demo.currency.repository.CurrencyRepository;
import com.example.app.demo.nbp.service.INBPRestApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyServiceImpl implements ICurrencyService {
    @Autowired
    CurrencyRepository currencyRepository;

    @Autowired
    INBPRestApiService nbpService;

    @Override
    public Double getConvertedValueFromLocalDB(String basicCode, String convertedCode, Double value, LocalDate date) {
        if (CurrencyValidation.isDataCorrect(basicCode, convertedCode, value, date)) {
            updateLocalDB(date);
            Optional<Float> optionalCurrencyRatio = currencyRepository.getRatioOfCurrency(basicCode, convertedCode, date);

            if (optionalCurrencyRatio.isPresent()) {
                return convertValue(value, optionalCurrencyRatio.get());
            }

            optionalCurrencyRatio = Optional.ofNullable(getReversedConvertedValueFromLocalDB(basicCode, convertedCode, date));
            if (optionalCurrencyRatio.isPresent()) {
                return convertValue(value, optionalCurrencyRatio.get());
            }

            optionalCurrencyRatio = Optional.ofNullable(getConvertedValue(basicCode, convertedCode, date));
            if (optionalCurrencyRatio.isPresent()) {
                return convertValue(value, optionalCurrencyRatio.get());
            }

        }
        return null;
    }

    private Float getReversedConvertedValueFromLocalDB(String basicCode, String convertedCode, LocalDate date) {
        Optional<Float> optionalReversedCurrencyRatio = currencyRepository.getRatioOfCurrency(convertedCode, basicCode, date);
        if (optionalReversedCurrencyRatio.isPresent()) {
            Float reverseCurrencyRatio = optionalReversedCurrencyRatio.get();
            return 1 / reverseCurrencyRatio;
        }
        return null;
    }

    private Float getConvertedValue(String basicCode, String convertedCode, LocalDate date) {
        Optional<Float> optionalDefaultBasicCurrencyRatio = currencyRepository.getRatioOfCurrency(basicCode, CurrencyCode.PLN.name(), date);
        Optional<Float> optionalDefaultConvertedCurrencyRatio = currencyRepository.getRatioOfCurrency(convertedCode, CurrencyCode.PLN.name(), date);
        if (optionalDefaultConvertedCurrencyRatio.isPresent() && optionalDefaultBasicCurrencyRatio.isPresent()) {
            Float defaultBasicCurrencyRatio = optionalDefaultBasicCurrencyRatio.get();
            Float defaultConvertedCurrencyRatio = optionalDefaultConvertedCurrencyRatio.get();
            return 1 / defaultConvertedCurrencyRatio * defaultBasicCurrencyRatio;
        }
        return null;
    }

    private void updateLocalDB(LocalDate date) {
        if (currencyRepository.findByCheckedData(date).isEmpty()) {
            List<Currency> currenciesFromApi = nbpService.getCurrencies(date);
            List<Currency> validatedCurrencies = new ArrayList<>();
            for (Currency currency : currenciesFromApi) {
                if (CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), currency.getCheckedData())) {
                    validatedCurrencies.add(currency);
                }
            }

            currencyRepository.saveAll(validatedCurrencies);
        }
    }

    private Double convertValue(Double value, Float currencyRatio) {
        double result = value * currencyRatio.doubleValue();
        result = Math.round(result * 100) / 100d;
        return result;
    }

}
