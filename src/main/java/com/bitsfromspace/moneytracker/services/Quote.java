package com.bitsfromspace.moneytracker.services;

/**
 * @author chris
 * @since 28/09/2016.
 */
public class Quote {
    private final String symbol;
    private final Double close;

    public Quote(String symbol, Double close) {
        this.symbol = symbol;
        this.close = close;
    }

    public String getSymbol() {
        return symbol;
    }

    public Double getClose() {
        return close;
    }
}
