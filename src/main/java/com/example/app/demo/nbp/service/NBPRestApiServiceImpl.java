package com.example.app.demo.nbp.service;

import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.model.Currency;
import com.example.app.demo.nbp.code.NBPApiCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class NBPRestApiServiceImpl implements INBPRestApiService {
    private static final String NBP_API_URL = "https://api.nbp.pl/api/exchangerates/tables/A/";

    @Override
    public List<Currency> getCurrencies(LocalDate date) {
        String dateToString = date.toString();
        return readDataFromURI(dateToString);
    }

    private List<Currency> readDataFromURI(String dateFormat) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(NBP_API_URL + dateFormat)).build();
            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::parseJsonToCurrencies)
                    .join();
        } catch (JSONException e) {
            return null;
        }
    }

    private List<Currency> parseJsonToCurrencies(String responseBody) {
        JSONArray albums = new JSONArray(responseBody);
        JSONObject obj = albums.getJSONObject(0);

        String date = obj.getString(NBPApiCode.EFFECTIVE_DATE.label);
        LocalDate checkedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        JSONArray currencies = obj.getJSONArray(NBPApiCode.RATES.label);
        List<Currency> currenciesList = new ArrayList<>();

        for (int i = 0; i < currencies.length(); i++) {
            JSONObject currencyFromApi = currencies.getJSONObject(i);
            String currencyCode = currencyFromApi.getString(NBPApiCode.CODE.label);
            Float convertedValue = currencyFromApi.getFloat(NBPApiCode.MID.label);

            Currency currency = new Currency(null, currencyCode, CurrencyCode.PLN.name(), convertedValue, checkedDate);
            currenciesList.add(currency);
        }
        return currenciesList;
    }
}
