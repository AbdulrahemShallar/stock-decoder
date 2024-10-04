package com.example.stock.decoder.model;

import lombok.Getter;

@Getter
public enum TimeSeriesType {
    MONTHLY("Monthly Time Series"),
    DAILY("Time Series (Daily)"),
    WEEKLY("Weekly Time Series");

    private final String key;

    TimeSeriesType(String key){this.key=key;}
}
