package com.bitsfromspace.moneytracker.model;

import com.bitsfromspace.moneytracker.services.Currency;

import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
public class Cash extends Asset {

    private final double initialAmount;
    private double interestPercentage;

    public Cash(String id, String name, LocalDate createDate, Currency currency, double initialAmount) {
        super(id, name, createDate, currency);
        this.initialAmount = initialAmount;
    }

    @Override
    public AssetType getType() {
        return AssetType.CASH;
    }

    @Override
    public Cash setLatestValue(AssetValue latestValue) {
        super.setLatestValue(latestValue);
        return this;
    }

    @Override
    public Cash setHighestValue(AssetValue highestValue) {
        super.setHighestValue(highestValue);
        return this;
    }

    @Override
    public Asset setLowestValue(AssetValue lowestValue) {
        super.setLowestValue(lowestValue);
        return this;
    }

    public double getInitialAmount() {
        return initialAmount;
    }

    public double getInterestPercentage() {
        return interestPercentage;
    }

    public Cash setInterestPercentage(double interestPercentage) {
        this.interestPercentage = interestPercentage;
        return this;
    }
}
