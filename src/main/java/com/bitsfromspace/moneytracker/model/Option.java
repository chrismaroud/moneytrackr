package com.bitsfromspace.moneytracker.model;

import com.bitsfromspace.moneytracker.services.Currency;

import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class Option extends Share {

    private final double strikePrice;

    public Option(String id, LocalDate createDate, Currency currency, String bbSymbol, double strikePrice) {
        super(id, createDate, currency, bbSymbol);
        this.strikePrice = strikePrice;
    }

    @Override
    public AssetType getType() {
        return AssetType.OPTION;
    }

    @Override
    public Option setLatestValue(AssetValue latestValue) {
        super.setLatestValue(latestValue);
        return this;
    }

    @Override
    public Option setHighestValue(AssetValue highestValue) {
        super.setHighestValue(highestValue);
        return this;
    }

    @Override
    public Option setLowestValue(AssetValue lowestValue) {
        super.setLowestValue(lowestValue);
        return this;
    }

    @Override
    public Option setNumberOfShares(double numberOfShares) {
        super.setNumberOfShares(numberOfShares);
        return this;
    }

    public double getStrikePrice() {
        return strikePrice;
    }
}
