package com.example.app.demo.currency.validation;


import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.factory.CurrencyFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class CurrencyValidationTest {

    @Test
    void dateIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        currency.setCheckedData(LocalDate.now().plusDays(1));
        Assertions.assertFalse(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }

    @Test
    void valueIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        currency.setConvertedValue(0f);
        Assertions.assertFalse(CurrencyValidation.isCurrencyCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(), Double.valueOf(currency.getConvertedValue()),currency.getCheckedData()));
    }

    @Test
    void codeIsNotValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes("TES", "TEST");
        Assertions.assertFalse(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }

    @Test
    void dataIsValid(){
        var currency = CurrencyFactory.createRandomCurrencyWithCodes(CurrencyCode.KRW.name(), CurrencyCode.TRY.name());
        Assertions.assertTrue(CurrencyValidation.isCurrencyDataCorrect(currency.getCurrencyCode(),currency.getConvertedCurrencyCode(),currency.getCheckedData()));
    }
}
