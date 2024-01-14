package com.example.app.demo.nbp.code;

public enum NBPApiCode {
    EFFECTIVE_DATE("effectiveDate"),
    CODE("code"),
    RATES("rates"),
    MID("mid");

    public final String label;

    NBPApiCode(String label) {
        this.label = label;
    }
}
