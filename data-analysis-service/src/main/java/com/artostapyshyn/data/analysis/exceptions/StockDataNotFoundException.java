package com.artostapyshyn.data.analysis.exceptions;

public class StockDataNotFoundException extends RuntimeException {
    public StockDataNotFoundException(String message) {
        super(message);
    }
}
