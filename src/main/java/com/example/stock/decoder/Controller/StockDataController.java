/**
 * StockDataController provides RESTful endpoints for retrieving stock data
 * and predicting stock movements. It handles incoming HTTP requests related to
 * fetching stock data and generating stock movement predictions using the
 * StockDataService and StockPredictionService.
 */
package com.example.stock.decoder.Controller;

import com.example.stock.decoder.model.MonthlyStockModel;
import com.example.stock.decoder.service.StockDataService;
import com.example.stock.decoder.service.StockPredictionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/stocks")
public class StockDataController {
    @Autowired
    private StockDataService stockDataService;

    @Autowired
    private StockPredictionService stockPredictionService;


    /**
     * Retrieves the monthly stock data for a given stock symbol from the Alpha Vantage API.
     *
     * @param symbol The stock symbol to fetch data for (e.g., AAPL, TSLA).
     * @param apiKey The API key for authenticating with the Alpha Vantage API.
     * @return A map where the key is the date and the value is a MonthlyStockData
     *         object containing stock data (open, high, low, close, volume) for that date.
     */
    @GetMapping("/monthly")
    public Map<String, MonthlyStockModel> getMonthlyStockData(@RequestParam String symbol, @RequestParam String apiKey) {
        return stockDataService.fetchMonthlyStockData(symbol, apiKey);
    }

    /**
     * Predicts whether the stock will go "up" or "down" based on the historical
     * stock data fetched from the Alpha Vantage API.
     *
     * @param symbol The stock symbol to fetch data for (e.g., AAPL, TSLA).
     * @param apiKey The API key for authenticating with the Alpha Vantage API.
     * @return A string prediction indicating whether the stock is expected to go "up" 📈 or "down" 📉.
     * @throws Exception In case of an error during the prediction process.
     */
    @GetMapping("/predict")
    public String predictStockMovement(@RequestParam String symbol, @RequestParam String apiKey) throws Exception {
        Map<String, MonthlyStockModel> stockData = stockDataService.fetchMonthlyStockData(symbol, apiKey);
        return stockPredictionService.predictStockMovement(stockData);
    }
}