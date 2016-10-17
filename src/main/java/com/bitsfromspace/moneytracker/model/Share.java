package com.bitsfromspace.moneytracker.model;

import com.bitsfromspace.moneytracker.services.Currency;

import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class Share extends Asset {

    private final String bbSymbol;
    private double numberOfShares;

    public Share(String id, String name, LocalDate createDate, Currency currency, String bbSymbol) {
        super(id, name, createDate, currency);
        this.bbSymbol = bbSymbol;
    }

    @Override
    public AssetType getType() {
        return AssetType.SHARE;
    }

    @Override
    public Asset setLatestValue(AssetValue latestValue) {
        super.setLatestValue(latestValue);
        return this;
    }

    @Override
    public Asset setHighestValue(AssetValue highestValue) {
        super.setHighestValue(highestValue);
        return this;
    }

    @Override
    public Asset setLowestValue(AssetValue lowestValue) {
        super.setLowestValue(lowestValue);
        return this;
    }

    public String getBbSymbol() {
        return bbSymbol;
    }

    public double getNumberOfShares() {
        return numberOfShares;
    }

    public Share setNumberOfShares(double numberOfShares) {
        this.numberOfShares = numberOfShares;
        return this;
    }
}
