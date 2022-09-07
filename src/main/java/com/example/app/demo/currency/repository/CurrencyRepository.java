package com.example.app.demo.currency.repository;

import com.example.app.demo.currency.model.Currency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CurrencyRepository extends JpaRepository<Currency, Long>, CurrencyRepositoryCustom {
    List<Currency> findByCheckedData(LocalDate checkedData);
}
