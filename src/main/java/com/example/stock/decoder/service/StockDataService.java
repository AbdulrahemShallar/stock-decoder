/**
 * StockDataService is responsible for fetching and processing monthly stock data
 * from the Alpha Vantage API. The service retrieves time series data based on
 * the stock symbol and API key provided, processes the response, and returns
 * the parsed stock data in a map format.
 */
package com.example.stock.decoder.service;

import com.example.stock.decoder.model.StockDataModel;
import com.example.stock.decoder.model.TimeSeriesType;
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
     *         StockDataModel object containing the stock's open, high, low, close,
     *         and volume for that date.
     */
    public Map<String, StockDataModel> fetchStockData(String symbol, String timeSeriesType, String apiKey) {
        RestTemplate restTemplate = new RestTemplate();

        // Build the request URL
        String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("function", timeSeriesType)
                .queryParam("symbol", symbol)
                .queryParam("apikey", apiKey)
                .toUriString();

        // Make the API request
        Map<String,Object> response = restTemplate.getForObject(url, Map.class);

        // Extract the time series data
        Map<String, StockDataModel> timeSeries = new HashMap<>();

        Map<String, Map<String, String>> timeSeriesData = fetchTimeSeriesData(response);


// Add a check to ensure timeSeriesData isn't null before processing
        if (timeSeriesData == null) {
            throw new RuntimeException("No valid time series data found in the response");
        }
        // Process the response and populate the time series data
        for (Map.Entry<String, Map<String, String>> entry : timeSeriesData.entrySet()) {
            String date = entry.getKey();
            StockDataModel stockData = getStockDataModel(entry, date);

            timeSeries.put(date, stockData);
        }

        return timeSeries;
    }

    private static StockDataModel getStockDataModel(Map.Entry<String, Map<String, String>> entry, String date) {
        Map<String, String> stockValues = entry.getValue();

        StockDataModel stockData = new StockDataModel();
        stockData.setDate(date);
        stockData.setOpen(Double.parseDouble(stockValues.get("1. open")));
        stockData.setHigh(Double.parseDouble(stockValues.get("2. high")));
        stockData.setLow(Double.parseDouble(stockValues.get("3. low")));
        stockData.setClose(Double.parseDouble(stockValues.get("4. close")));
        stockData.setVolume(Long.parseLong(stockValues.get("5. volume")));
        return stockData;
    }

    // Method to fetch the time series data based on available keys in the response
    private Map<String, Map<String, String>> fetchTimeSeriesData(Map<String, Object> response) {
        // Iterate over the enum values to check for the available time series key
        for (TimeSeriesType type : TimeSeriesType.values()) {
            if (response.get(type.getKey()) != null) {
                return (Map<String, Map<String, String>>) response.get(type.getKey());
            }
        }

        // If no valid time series data is found, throw an exception
        throw new RuntimeException("No valid time series data found in the response");
    }
}