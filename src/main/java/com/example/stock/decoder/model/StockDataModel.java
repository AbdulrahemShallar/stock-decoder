package com.example.stock.decoder.model;

import lombok.Data;

@Data
public class StockDataModel {
    private String date;
    private double open;
    private double high;
    private double low;
    private double close;
    private long volume;
}