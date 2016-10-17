package com.bitsfromspace.moneytracker.model;

import com.bitsfromspace.moneytracker.services.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Document
public abstract class Asset {
    @Id
    private final String id;
    private final String name;
    private final Currency currency;
    private final LocalDate createDate;

    private AssetValue createValue;
    private AssetValue latestValue;
    private AssetValue highestValue;
    private AssetValue lowestValue;

    protected Asset(String id, String name, LocalDate createDate, Currency currency) {
        this.id = id;
        this.name = name;
        this.createDate = createDate;
        this.currency = currency;
    }


    public abstract AssetType getType();

    public String getId() {
        return id;
    }

    public LocalDate getCreateDate() {
        return createDate;
    }

    public Currency getCurrency() {
        return currency;
    }

    public AssetValue getLatestValue() {
        return latestValue;
    }

    public AssetValue getCreateValue() {
        return createValue;
    }
    public Asset setCreateValue(AssetValue createValue){
        this.createValue =createValue;
        return this;
    }

    public Asset setLatestValue(AssetValue latestValue) {
        this.latestValue = latestValue;
        return this;
    }

    public AssetValue getHighestValue() {
        return highestValue;
    }

    public Asset setHighestValue(AssetValue highestValue) {
        this.highestValue = highestValue;
        return this;
    }

    public AssetValue getLowestValue() {
        return lowestValue;
    }

    public Asset setLowestValue(AssetValue lowestValue) {
        this.lowestValue = lowestValue;
        return this;
    }

    public String getName() {
        return name;
    }
}
