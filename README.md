# Stock Price Prediction API

This project is a RESTful web service for retrieving stock data and predicting stock movements using historical data. The service interacts with the Alpha Vantage API for stock data and applies machine learning (via the Weka library) to predict whether a stock's price will go "up" 📈 or "down" 📉.

## Features

- **Fetch Stock Data**: Retrieve stock data for symbols like AAPL, TSLA, etc., with support for different time series (Daily, Weekly, Monthly).
- **Predict Stock Movement**: Predict whether a stock will rise or fall based on historical data using machine learning classifiers (e.g., J48 decision tree).
- **Expandable Architecture**: Future updates will include more detailed predictions (stock price in numbers) and additional search features like custom date ranges for prediction analysis.

## Endpoints

### 1. Get Stock Data
Retrieves the stock data for a given symbol and time series (e.g., Daily, Weekly, Monthly).
  
**URL**: `/api/stocks/data`

**Method**: `GET`

**Parameters**:
- `symbol` (String): Stock symbol (e.g., AAPL, TSLA).
- `timeSeriesType` (String): Time series type (e.g., TIME_SERIES_DAILY, TIME_SERIES_MONTHLY).
- `apiKey` (String): Your Alpha Vantage API key.

**Response**:  
Returns a map of dates to stock data, including open, high, low, close prices, and volume.

### 2. Predict Stock Movement
Predicts whether a stock will go "up" or "down" based on historical data.

**URL**: `/api/stocks/predict`

**Method**: `GET`

**Parameters**:
- `symbol` (String): Stock symbol.
- `timeSeriesType` (String): Time series type.
- `apiKey` (String): Your Alpha Vantage API key.

**Response**:  
Returns a prediction string: "Stock going up 📈" or "Stock going down 📉".

## Future Enhancements

1. **Numeric Price Prediction**: In addition to "up" or "down" movements, predict the exact stock price based on machine learning algorithms.
2. **Instant Predictions**: Real-time stock movement predictions as data is received.
3. **Enhanced Search Features**:
   - Specify the duration of historical data to base predictions on (e.g., last 30 days, last year).
   - Search stock data by custom date ranges for deeper analysis.

## Tech Stack

- **Java Spring Boot**: REST API development.
- **Weka Library**: Machine learning for stock movement predictions.
- **Alpha Vantage API**: Stock data provider.
- **MySQL/PostgreSQL** (Future): Database integration for storing historical data and user preferences.
