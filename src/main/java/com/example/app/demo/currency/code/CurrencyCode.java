package com.example.app.demo.currency.code;

public enum CurrencyCode {
    THB("bat"),
    USD("dolar amerykański"),
    AUD("dolar australijski"),
    HKD("dolar Hongkongu"),
    CAD("dolar kanadyjski"),
    NZD("dolar nowozelandzki"),
    SGD("dolar singapurski"),
    EUR("euro"),
    HUF("forint"),
    CHF("frank szwajcarski"),
    GBP("funt szterling"),
    UAH("hrywna"),
    JPY("jen"),
    CZK("korona czeska"),
    DKK("korona dunska"),
    ISK("korona islandzka"),
    NOK("korona norweska"),
    SEK("korona szwedzka"),
    HRK("kuna"),
    RON("lej rumunski"),
    BGN("lew"),
    TRY("lira turecka"),
    ILS("nowy izraelski szekel"),
    CLP("peso chilijskie"),
    PHP("peso filipinskie"),
    MXN("peso meksykanskie"),
    ZAR("rand"),
    BRL("real"),
    MYR("ringgit"),
    IDR("rupia indonezyjska"),
    INR("rupia indyjska"),
    KRW("won południowokoreanski"),
    CNY("yuan renminbi"),
    XDR("SDR"),
    PLN("zloty");

    public final String label;

    CurrencyCode(String label) {
        this.label = label;
    }
}
