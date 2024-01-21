package com.example.app.demo.currency.service;

import com.example.app.demo.common.exceptions.NotFoundCurrency;
import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.factory.CurrencyFactory;
import com.example.app.demo.currency.repository.CurrencyRepository;
import com.example.app.demo.nbp.service.NBPRestApiServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
public class CurrencyServiceTest {
    @Mock
    CurrencyRepository currencyRepository;

    @InjectMocks
    CurrencyServiceImpl currencyService;

    @Mock
    NBPRestApiServiceImpl nbpRestApiService;


    @BeforeEach
    void setUp() {
        currencyRepository.deleteAll();
    }

    @Test
    void shouldFindCurrencyInLocalDB() {
        //given
        MockitoAnnotations.initMocks(this);
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.PLN.name(), CurrencyCode.AUD.name());
        Mockito.when(currencyRepository.getRatioOfCurrency(CurrencyCode.PLN.name(), CurrencyCode.AUD.name(), LocalDate.now())).thenReturn(Optional.of(currency.getConvertedValue()));
        Mockito.when(currencyRepository.saveAll(any())).thenReturn(new ArrayList<>());
        Mockito.when(nbpRestApiService.getCurrencies(any())).thenReturn(new ArrayList<>());

        //when
        Double convertedValue = currencyService.getConvertedCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), 10d,currency.getCheckedData());

        //then
        assertEquals(convertValue(10d * currency.getConvertedValue()), convertedValue);
    }

    @Test
    void shouldFindValueInLocalDB() {
        //given
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        MockitoAnnotations.initMocks(this);
        Mockito.when(nbpRestApiService.getCurrencies(any())).thenReturn(new ArrayList<>());
        Mockito.when(currencyRepository.getRatioOfCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), currency.getCheckedData())).thenReturn(Optional.of(currency.getConvertedValue()));
        Mockito.when(currencyRepository.findByCheckedData(LocalDate.now())).thenReturn(new ArrayList<>());

        //when
        Double currencyRatio =currencyService.getConvertedCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), 2d,currency.getCheckedData());

        //then
        assertEquals(convertValue(currency.getConvertedValue() * 2d), currencyRatio);

    }

    @Test
    void shouldFindReversedValueInLocalDB() {
        //given
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        MockitoAnnotations.initMocks(this);
        Mockito.when(currencyRepository.findByCheckedData(LocalDate.now())).thenReturn(List.of(currency));
        Mockito.when(currencyRepository.getRatioOfCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), currency.getCheckedData())).thenReturn(Optional.empty());
        Mockito.when(currencyRepository.getRatioOfCurrency(currency.getConvertedCurrencyCode(), currency.getCurrencyCode(), currency.getCheckedData())).thenReturn(Optional.of(currency.getConvertedValue()));

        //when
        Double currencyRatio =currencyService.getConvertedCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), 20d,currency.getCheckedData());

        //then
        assertEquals(convertValue(1 / currency.getConvertedValue() *  20d), currencyRatio);

    }

    @Test
    void shouldFillDBAndFindValueBetweenTwoForeignCurrencies() {
        //given
        var dollarRatio = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.USD.name(), CurrencyCode.PLN.name());
        var euroRatio = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.EUR.name(), CurrencyCode.PLN.name());
        var currencyToFind = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.EUR.name(), CurrencyCode.USD.name());
        MockitoAnnotations.initMocks(this);
        Mockito.when(nbpRestApiService.getCurrencies(any())).thenReturn(List.of(euroRatio, dollarRatio));
        Mockito.when(currencyRepository.findByCheckedData(LocalDate.now())).thenReturn(new ArrayList<>());
        Mockito.when(currencyRepository.getRatioOfCurrency(CurrencyCode.USD.name(), CurrencyCode.EUR.name(), dollarRatio.getCheckedData())).thenReturn(Optional.empty());
        Mockito.when(currencyRepository.getRatioOfCurrency(CurrencyCode.EUR.name(), CurrencyCode.USD.name(), dollarRatio.getCheckedData())).thenReturn(Optional.empty());
        Mockito.when(currencyRepository.getRatioOfCurrency(CurrencyCode.USD.name(), CurrencyCode.PLN.name(), dollarRatio.getCheckedData())).thenReturn(Optional.of(dollarRatio.getConvertedValue()));
        Mockito.when(currencyRepository.getRatioOfCurrency(CurrencyCode.EUR.name(), CurrencyCode.PLN.name(), dollarRatio.getCheckedData())).thenReturn(Optional.of(euroRatio.getConvertedValue()));

        //when
        Double currencyRatio =currencyService.getConvertedCurrency(currencyToFind.getCurrencyCode(), currencyToFind.getConvertedCurrencyCode(), 2d,currencyToFind.getCheckedData());

        //then
        assertEquals(convertValue(2d / dollarRatio.getConvertedValue() * euroRatio.getConvertedValue()), currencyRatio);

    }


    @Test
    void shouldThrowNotFoundExceptionCausedByIncorrectCode() {
        //given
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(),"TES");

       //when
        Exception ex = Assertions.assertThrows(NotFoundCurrency.class, ()->{
            currencyService.getConvertedCurrency(currency.getCurrencyCode(), currency.getConvertedCurrencyCode(), 2d,currency.getCheckedData());
        } );

        //then
        assertTrue(ex.getMessage().contains("Currency not found"));

    }

    private Double convertValue(Double value) {
        return Math.round(value * 100) / 100d;
    }

}
