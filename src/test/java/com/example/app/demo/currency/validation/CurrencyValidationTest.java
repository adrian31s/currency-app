package com.example.app.demo.currency.validation;


import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.factory.CurrencyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class CurrencyValidationTest {

    @Test
    void dateIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        currency.setCheckedData(LocalDate.now().plusDays(1));
        assertFalse(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }

    @Test
    void valueIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        currency.setConvertedValue(0f);
        assertFalse(CurrencyValidation.isCurrencyCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(), Double.valueOf(currency.getConvertedValue()),currency.getCheckedData()));
    }

    @Test
    void codeIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes("TES", "TEST");
        assertFalse(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }

    @Test
    void dataIsValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        assertTrue(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }
}
