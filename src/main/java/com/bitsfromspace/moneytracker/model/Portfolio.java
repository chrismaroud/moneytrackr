package com.bitsfromspace.moneytracker.model;

import com.bitsfromspace.moneytracker.services.Currency;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chris
 * @since 29/09/2016.
 */
@Document
public class Portfolio {
    @Id
    private final String email;
    private final Currency currency;
    private final LocalDate createDate;

    private PortfolioValue latestValue;
    private PortfolioValue lowestValue;
    private PortfolioValue highestValue;
    private List<Asset> assets;

    public Portfolio(@NotNull String email, @NotNull Currency currency, @NotNull LocalDate createDate) {
        this.email = email;
        this.currency = currency;
        this.createDate = createDate;
    }

    public @NotNull List<Asset> getAssets() {
        return assets == null ? Collections.emptyList() : assets;
    }

    public void setAssets(List<Asset> assets) {
        this.assets = assets;
    }

    public
    @NotNull
    String getEmail() {
        return email;
    }

    public
    @NotNull
    Currency getCurrency() {
        return currency;
    }

    public
    @NotNull
    LocalDate getCreateDate() {
        return createDate;
    }

    public void addAsset(@NotNull Asset asset){
        final List<Asset> copy = new ArrayList<>(getAssets());
        copy.add(asset);
        setAssets(copy);
    }

    public PortfolioValue getLatestValue() {
        return latestValue;
    }

    public Portfolio setLatestValue(PortfolioValue latestValue) {
        this.latestValue = latestValue;
        return this;
    }

    public PortfolioValue getLowestValue() {
        return lowestValue;
    }

    public Portfolio setLowestValue(PortfolioValue lowestValue) {
        this.lowestValue = lowestValue;
        return this;
    }

    public PortfolioValue getHighestValue() {
        return highestValue;
    }

    public Portfolio setHighestValue(PortfolioValue highestValue) {
        this.highestValue = highestValue;
        return this;
    }
}