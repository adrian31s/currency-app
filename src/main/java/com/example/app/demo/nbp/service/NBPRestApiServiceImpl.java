package com.example.app.demo.nbp.service;

import com.example.app.demo.currency.code.CurrencyCode;
import com.example.app.demo.currency.model.Currency;
import com.example.app.demo.nbp.code.NBPApiCode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionException;

@Service
public class NBPRestApiServiceImpl implements INBPRestApiService {
    private static final String apiUrl = "https://api.nbp.pl/api/exchangerates/tables/A/";

    @Override
    public List<Currency> getCurrencies(LocalDate date) {
        String dateToString = date.toString();
        return readDataFromURI(dateToString);
    }

    private List<Currency> readDataFromURI(String dateFormat) {
        HttpClient client = HttpClient.newHttpClient();
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl + dateFormat)).build();

            return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(HttpResponse::body)
                    .thenApply(this::parseJsonToCurrencies)
                    .join();
        } catch (CompletionException | IllegalArgumentException | IllegalStateException e) {
            return null;
        }
    }

    private List<Currency> parseJsonToCurrencies(String responseBody) {
        JSONArray albums = new JSONArray(responseBody);
        JSONObject obj = albums.getJSONObject(0);
        LocalDate checkedDate;
        try {
            String date = obj.getString(NBPApiCode.EFFECTIVE_DATE.label);
            checkedDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (JSONException | DateTimeParseException e) {
            e.printStackTrace();
            return List.of();
        }
        List<Currency> currenciesList = new ArrayList<>();
        JSONArray currencies = obj.getJSONArray(NBPApiCode.RATES.label);
        for (int i = 0; i < currencies.length(); i++) {
            try {
                JSONObject currencyFromApi = currencies.getJSONObject(i);
                String currencyCode = currencyFromApi.getString(NBPApiCode.CODE.label);
                Float value = currencyFromApi.getFloat(NBPApiCode.MID.label);

                Currency currency = new Currency();
                currency.setCurrencyCode(currencyCode);
                currency.setConvertedCurrencyCode(CurrencyCode.PLN.name());
                currency.setConvertedValue(value);
                currency.setCheckedData(checkedDate);
                currenciesList.add(currency);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return currenciesList;
    }
}
