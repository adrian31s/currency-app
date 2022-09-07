package com.example.app.demo.currency.repository;

import com.example.app.demo.currency.model.Currency;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.Optional;


@Repository
public class CurrencyRepositoryImpl implements CurrencyRepositoryCustom {
    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Optional<Float> getRatioOfCurrency(String basicCurrencyCode, String convertedCurrencyCode, LocalDate date) {
        try {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaQuery<Float> cq = cb.createQuery(Float.class);
            Root<Currency> root = cq.from(Currency.class);
            cq.select(root.get("convertedValue"));
            cq.where(cb.and(
                    cb.equal(root.get("currencyCode"), basicCurrencyCode),
                    cb.equal(root.get("convertedCurrencyCode"), convertedCurrencyCode),
                    cb.equal(root.get("checkedData"), date)
            ));
            return Optional.of(entityManager.createQuery(cq).getSingleResult());
        } catch (NoResultException | NullPointerException e) {
            return Optional.empty();
        }
    }

}
