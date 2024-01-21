package com.example.app.demo.currency.service;

import com.example.app.demo.common.exceptions.NotFoundCurrency;
import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.model.Currency;
import com.example.app.demo.currency.repository.CurrencyRepository;
import com.example.app.demo.currency.validation.CurrencyValidation;
import com.example.app.demo.nbp.service.INBPRestApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import static com.example.app.demo.currency.utils.Util.calculateCurrencyValue;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrencyServiceImpl implements ICurrencyService {
    private final CurrencyRepository currencyRepository;
    private final INBPRestApiService nbpService;

    @Override
    public Double getConvertedCurrency(String basicCode, String convertedCode, Double value, LocalDate date) {
        if (CurrencyValidation.isCurrencyCorrect(basicCode, convertedCode, value, date)) {
            updateLocalDB(date);
            Optional<Float> optionalCurrencyRatio = currencyRepository.getRatioOfCurrency(basicCode, convertedCode, date);

            if (optionalCurrencyRatio.isPresent()) {
                return calculateCurrencyValue(value, optionalCurrencyRatio.get());
            }

            optionalCurrencyRatio = getReversedConvertedValueFromLocalDB(basicCode, convertedCode, date);
            if (optionalCurrencyRatio.isPresent()) {
                return calculateCurrencyValue(value, optionalCurrencyRatio.get());
            }

            optionalCurrencyRatio = getConvertedValue(basicCode, convertedCode, date);
            if (optionalCurrencyRatio.isPresent()) {
                return calculateCurrencyValue(value, optionalCurrencyRatio.get());
            }

        }
        throw new NotFoundCurrency();
    }

    // to avoid calling nbp api too many times, check if in local database reversed currency (basic and converted) is present
    private Optional<Float> getReversedConvertedValueFromLocalDB(String basicCode, String convertedCode, LocalDate date) {
        Optional<Float> optionalReversedCurrencyRatio = currencyRepository.getRatioOfCurrency(convertedCode, basicCode, date);
        if (optionalReversedCurrencyRatio.isPresent()) {
            Float reverseCurrencyRatio = optionalReversedCurrencyRatio.get();
            return Optional.of(1 / reverseCurrencyRatio);
        }
        return Optional.empty();
    }

    // convert reversed to searched value 
    private Optional<Float> getConvertedValue(String basicCode, String convertedCode, LocalDate date) {
        Optional<Float> optionalDefaultBasicCurrencyRatio = currencyRepository.getRatioOfCurrency(basicCode, CurrencyCode.PLN.name(), date);
        Optional<Float> optionalDefaultConvertedCurrencyRatio = currencyRepository.getRatioOfCurrency(convertedCode, CurrencyCode.PLN.name(), date);
        if (optionalDefaultConvertedCurrencyRatio.isPresent() && optionalDefaultBasicCurrencyRatio.isPresent()) {
            Float defaultBasicCurrencyRatio = optionalDefaultBasicCurrencyRatio.get();
            Float defaultConvertedCurrencyRatio = optionalDefaultConvertedCurrencyRatio.get();
            return Optional.of(1 / defaultConvertedCurrencyRatio * defaultBasicCurrencyRatio);
        }
        return Optional.empty();
    }

    // updates local database from npb api, if currency of given data is not present
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
}
