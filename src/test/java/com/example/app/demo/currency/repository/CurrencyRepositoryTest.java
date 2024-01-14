package com.example.app.demo.currency.repository;


import com.example.app.demo.currency.factory.CurrencyFactory;
import com.example.app.demo.currency.model.Currency;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
public class CurrencyRepositoryTest {
    @Autowired
    CurrencyRepository currencyRepository;

    @BeforeEach
    void setUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void shouldSaveCurrencyToLocalDB() {
        //given
        var currency = CurrencyFactory.createRandomCurrency();
        var currency1 = CurrencyFactory.createRandomCurrency();

        //when
        currencyRepository.save(currency);
        currencyRepository.save(currency1);

        //then
        Assertions.assertEquals(2, currencyRepository.findAll().size());
    }

    @Test
    void shouldFindCurrencyRatioByCodeAndByDate() {
        //given
        var currency = CurrencyFactory.createRandomCurrency();

        //when
        currencyRepository.save(currency);

        //then
        Assertions.assertTrue(currencyRepository.getRatioOfCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), currency.getCheckedData()).isPresent());
        Assertions.assertEquals(1, currencyRepository.findByCheckedData(LocalDate.now()).size());
    }

    @Test
    void shouldReturnEmptyResultWhenMissingDateInDB() {
        //given
        var currency = CurrencyFactory.createRandomCurrency();
        var currency1 = CurrencyFactory.createRandomCurrency();
        LocalDate missingDate = LocalDate.now();
        missingDate = missingDate.minusDays(1);
        currencyRepository.save(currency);
        currencyRepository.save(currency1);

        //when
        List<Currency> currenciesFromLocalDb = currencyRepository.findByCheckedData(missingDate);

        //then
        Assertions.assertEquals(0, currenciesFromLocalDb.size());
    }

}
