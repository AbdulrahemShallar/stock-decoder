/**
 * StockDataService is responsible for fetching and processing monthly stock data
 * from the Alpha Vantage API. The service retrieves time series data based on
 * the stock symbol and API key provided, processes the response, and returns
 * the parsed stock data in a map format.
 */
package com.example.stock.decoder.service;

import com.example.stock.decoder.model.MonthlyStockModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;

@Service
public class StockDataService {


    @Value("${stock.api.url}")
    private String apiUrl;  // Inject the URL from application.properties

    /**
     * Fetches monthly stock data for the given stock symbol by making a REST API
     * request to the Alpha Vantage API. The response contains time series data
     * representing stock performance for each month.
     *
     * @param symbol The stock symbol to fetch data for (e.g., AAPL, TSLA).
     * @param apiKey The API key for authentication with the Alpha Vantage API.
     * @return A map where the key is the date (as a string) and the value is a
     *         MonthlyStockModel object containing the stock's open, high, low, close,
     *         and volume for that date.
     */
    public Map<String, MonthlyStockModel> fetchMonthlyStockData(String symbol, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the request URL
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("function", "TIME_SERIES_MONTHLY")
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .toUriString();

        // Make the API request
        Map response = restTemplate.getForObject(url, Map.class);

        // Extract the time series data
        Map<String, MonthlyStockModel> timeSeries = new HashMap<>();
        Map<String, Map<String, String>> timeSeriesData = (Map<String, Map<String, String>>) response.get("Monthly Time Series");

        // Process the response and populate the time series data
        for (Map.Entry<String, Map<String, String>> entry : timeSeriesData.entrySet()) {
            String date = entry.getKey();
            Map<String, String> stockValues = entry.getValue();

            MonthlyStockModel stockData = new MonthlyStockModel();
            stockData.setDate(date);
            stockData.setOpen(Double.parseDouble(stockValues.get("1. open")));
            stockData.setHigh(Double.parseDouble(stockValues.get("2. high")));
            stockData.setLow(Double.parseDouble(stockValues.get("3. low")));
            stockData.setClose(Double.parseDouble(stockValues.get("4. close")));
            stockData.setVolume(Long.parseLong(stockValues.get("5. volume")));

            timeSeries.put(date, stockData);
        }

        return timeSeries;
    }
}