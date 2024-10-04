/**
 * StockPredictionService is responsible for predicting stock movements based on historical
 * monthly stock data using machine learning classifiers. It uses the Weka library to create
 * datasets from the stock data and make predictions whether the stock is going "up" or "down".
 */
package com.example.stock.decoder.service;

import com.example.stock.decoder.model.StockDataModel;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StockPredictionService {

    /**
     * Predicts whether the stock is going "up" 📈 or "down" 📉 based on the historical stock data.
     * The method uses a Weka Classifier (e.g., J48 decision tree) to analyze the stock data and
     * make predictions.
     *
     * @param stockData A map containing monthly stock data (open, high, low, close, volume) for each month.
     * @return A string indicating whether the stock is predicted to go "up" 📈 or "down" 📉.
     * @throws Exception If an error occurs during the dataset creation or classification process.
     */
    public String predictStockMovement(Map<String, StockDataModel> stockData) throws Exception {
        // Create a dataset from the fetched stock data
        Instances dataset = createDataset(stockData);

        // Train a classifier on the dataset
        Classifier classifier = new J48();  // You can use RandomForest or others as well
        classifier.buildClassifier(dataset);

        // Create an instance for the latest month to predict its movement
        StockDataModel latestData = getLatestStockData(stockData);
        DenseInstance instance = new DenseInstance(6);
        instance.setValue(dataset.attribute("open"), latestData.getOpen());
        instance.setValue(dataset.attribute("high"), latestData.getHigh());
        instance.setValue(dataset.attribute("low"), latestData.getLow());
        instance.setValue(dataset.attribute("close"), latestData.getClose());
        instance.setValue(dataset.attribute("volume"), latestData.getVolume());
        instance.setDataset(dataset);

        // Classify the instance (predict "up" or "down")
        double result = classifier.classifyInstance(instance);
        return dataset.classAttribute().value((int) result).equals("up") ? "Stock going up 📈" : "Stock going down 📉";
          // Returns "up" or "down"
    }

    /**
     * Creates a Weka dataset from the given stock data. The dataset contains attributes such as
     * "open", "high", "low", "close", and "volume", and a class attribute indicating whether
     * the stock went "up" or "down".
     *
     * @param stockData A map containing stock data for each month.
     * @return An Instances object representing the dataset to be used for classification.
     */
    private Instances createDataset(Map<String, StockDataModel> stockData) {
        // Define attributes for Weka
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("open"));
        attributes.add(new Attribute("high"));
        attributes.add(new Attribute("low"));
        attributes.add(new Attribute("close"));
        attributes.add(new Attribute("volume"));

        // Class attribute ("up" or "down")
        List<String> classValues = new ArrayList<>();
        classValues.add("up");
        classValues.add("down");
        attributes.add(new Attribute("class", classValues));

        // Create dataset
        Instances dataset = new Instances("StockData", attributes, stockData.size());
        dataset.setClassIndex(dataset.numAttributes() - 1);

        // Populate the dataset with your stock data
        for (Map.Entry<String, StockDataModel> entry : stockData.entrySet()) {
            StockDataModel stock = entry.getValue();
            DenseInstance instance = new DenseInstance(6);
            instance.setValue(dataset.attribute("open"), stock.getOpen());
            instance.setValue(dataset.attribute("high"), stock.getHigh());
            instance.setValue(dataset.attribute("low"), stock.getLow());
            instance.setValue(dataset.attribute("close"), stock.getClose());
            instance.setValue(dataset.attribute("volume"), stock.getVolume());

            // Label it based on if the closing price is higher than opening (up/down)
            String movement = stock.getClose() > stock.getOpen() ? "up" : "down";
            instance.setValue(dataset.attribute("class"), movement);
            instance.setDataset(dataset);

            dataset.add(instance);
        }
        return dataset;
    }

    /**
     * Retrieves the latest stock data from the provided stock data map. This method is used to
     * extract the most recent month's data for stock movement prediction.
     *
     * @param stockData A map containing stock data for each month.
     * @return The latest MonthlyStockData object representing the most recent stock data.
     */
    private StockDataModel getLatestStockData(Map<String, StockDataModel> stockData) {
        // Assuming you want the latest date's stock data
        return stockData.values().stream().findFirst().orElse(null);
    }
}