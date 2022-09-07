package com.example.app.demo.currency.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Currency.TABLE_NAME)
public class Currency {
    public static final String TABLE_NAME = "CURRENCY";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "currency_id")
    private Long currencyId;

    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    @Column(name = "converted_currency_code", length = 3)
    private String convertedCurrencyCode;

    @Column(name = "converted_value")
    private Float convertedValue;

    @Column(name = "checked_data")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate checkedData;
}
