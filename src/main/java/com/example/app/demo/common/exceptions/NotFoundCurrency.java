package com.example.app.demo.common.exceptions;

public class NotFoundCurrency extends RuntimeException{
    public NotFoundCurrency() {
        super("Currency not found");
    }
}
